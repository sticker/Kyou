#!/usr/bin/perl

#########################################################
#
# 今日の注目ガール・おしゃれ男子 取得スクリプト
#
#  - オリコン トレンド トップページから情報を取得し
#    JSON形式で出力する
#  - 形式は以下のとおり
#
#    [
#       {
#           "url":"詳細URL",
#           "image":"画像URL",
#       },
#       ・・・
#    ]
#
#########################################################

use strict;
use warnings;

use YAML;
use Data::Dumper;
use Web::Scraper;
use LWP::UserAgent;
use URI;
use JSON;
use Encode;

my $url = 'http://www.oricon.co.jp/trend/';
my $uri = new URI($url);

#Scrape設定
my $scraper = scraper {
    process 'div.mainPhoto>p>a', 'result[]' => scraper {
        process 'a', 'url' => '@href';
        process 'a>img', 'image' => '@src';
    };
};

my $scraper_profile = scraper {
    process 'td.profile_box02', 'profile[]' => 'TEXT';
};

#UserAgentを変える
use LWP::UserAgent;
my $ua = new LWP::UserAgent( agent => 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)');
$scraper->user_agent($ua);

#外部サイトへリクエスト
my $response = $scraper->user_agent->get($url);
#print Dumper $response;

#外部サイトからエラー応答が返ってきたらERROR返却して終了
unless ($response->is_success) {
    print "ERROR:remote site is down.";
    exit 1;
}

#エンコード指定
my ($encoding) = $response->header('Content-Type') =~ /charset=([\w\-]+)/g;
#外部サイトからの応答をScrape
my $res = $scraper->scrape( Encode::decode($encoding, $response->content));

#print Dumper $res;
#print "===============================\n\n";

for(my $i=0; $i < scalar(@{%$res->{result}}); $i++){
    $res->{result}[$i]{'url'} = $uri->scheme . "://" . $uri->host . $res->{result}[$i]{'url'};

    #profileページ取得
    my $response_profile = $scraper_profile->user_agent->get($res->{result}[$i]{'url'});
    #外部サイトからエラー応答が返ってきたらERROR返却して終了
    unless ($response_profile->is_success) {
        print "ERROR:remote site is down.";
        exit 1;
    }

    #エンコード指定
    my ($encoding_profile) = $response_profile->header('Content-Type') =~ /charset=([\w\-]+)/g;
    #外部サイトからの応答をScrape
    my $res_profile = $scraper_profile->scrape( Encode::decode($encoding_profile, $response_profile->content));

#print Dumper $res_profile;
#print "===============================\n\n";

    #my $profile = $res_profile->{profile}[1] . substr($res_profile->{profile}[2],index($res_profile->{profile}[2], '（'));
    my $job = $res_profile->{profile}[1];
    my $old = $res_profile->{profile}[2];
    if ($old =~ /\d{4}\.\d{1,2}\.\d{1,2}(.*)$/ ){
        $old = $1;
    }
    my $profile = $job . $old;

    #$resにプロフィール情報を付加する
    $res->{result}[$i]{'profile'} = $profile;
}


#JSON形式で出力
my $json = new JSON;
#my $json_text = '[';
my $json_text = '';
$json_text .= $json->encode($res->{result}).', ';
$json_text =~ s/,.$//s; #最後のカンマを除去
#$json_text .= ']';  #JSONハッシュ配列の閉じ

print utf8::is_utf8($json_text) ? encode('utf-8', $json_text) : $json_text;
