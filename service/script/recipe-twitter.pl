#!/usr/bin/perl

#########################################################
#
# 毒女ニュース トップ記事取得スクリプト
#
#  - 毒女ニューストップページからトップ記事の情報を取得し
#    JSON形式で出力する
#  - 形式は以下のとおり
#
#    [
#       {
#           "url":"記事URL",
#           "title":"記事タイトル",
#           "image":"記事表紙画像URL"
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

my $url = 'https://twitter.com/intent/user?screen_name=tsubuyakirecipe';
my $output = "/var/www/html/Kyou/recipe.json";

#Scrape設定
my $scraper = scraper {
    process 'div.tweet-row', 'result[]' => scraper {
        process 'div.tweet-text', 'title' => 'TEXT';
        process 'div.tweet-text>a', 'url' => 'TEXT';
    };
};

#UserAgentを変える
use LWP::UserAgent;
my $ua = new LWP::UserAgent( agent => 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)');
$scraper->user_agent($ua);

#外部サイトへリクエスト
my $response = $scraper->user_agent->get($url);
#print Dumper $response;

#外部サイトからエラー応答が返ってきたらファイル出力せず終了
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


#JSON形式で出力
my $json = new JSON;
#my $json_text = '[';
my $json_text = '';
$json_text .= $json->encode($res->{result}).', ';
$json_text =~ s/,.$//s; #最後のカンマを除去
#$json_text .= ']';  #JSONハッシュ配列の閉じ

#print utf8::is_utf8($json_text) ? encode('utf-8', $json_text) : $json_text;

open(OUT, "> $output") || die "ERROR: $output $!\n";
print OUT utf8::is_utf8($json_text) ? encode('utf-8', $json_text) : $json_text;
