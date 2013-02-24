#!/usr/bin/perl

#########################################################
#
# ポジティブ名言取得スクリプト
#
#  - ポジティブ名言の情報を取得しCSV形式で出力する
#  - 形式は以下のとおり
#
#    text,auther
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

my $url = 'http://www.meigensyu.com/posts/index/page1.html';

#Scrape設定
my $scraper = scraper {
    process 'div.meigenbox', 'result[]' => scraper {
        process 'div.text', 'meigen' => 'TEXT';
        process 'div.link>ul>li', 'auther' => 'TEXT';
    };
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


#JSON形式で出力
#my $json = new JSON;
#my $json_text = '';
#$json_text .= $json->encode($res->{result}).', ';
#$json_text =~ s/,.$//s; #最後のカンマを除去
#print utf8::is_utf8($json_text) ? encode('utf-8', $json_text) : $json_text;

#CSVで出力
for(my $i = 0; $i < scalar(@{%$res->{result}}); $i++){
    #print "\"". encode('utf-8',$res->{result}[$i]{'meigen'}) ."\", \"". encode('utf-8',$res->{result}[$i]{'auther'}) . "\"\n";
    print encode('utf-8',$res->{result}[$i]{'meigen'}) .",". encode('utf-8',$res->{result}[$i]{'auther'}) . "\n";
}
