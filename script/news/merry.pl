#!/usr/bin/perl

#########################################################
#
# メリーニュース トップ記事取得スクリプト
#
#  - メリーニューストップページからトップ記事の情報を取得し
#    JSON形式で出力する
#  - 形式は以下のとおり
#
#    [
#       {
#           "url":"記事URL",
#           "title":"記事タイトル",
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

my $url = 'http://www.merryproject.com/news/';
my $uri = new URI($url);
my $output = "/var/www/html/Kyou/merry.json";

#Scrape設定
my $scraper = scraper {
    process 'div.newsEntryHeader', 'result[]' => scraper {
        process 'h2>a', 'url' => '@href', 'title' => 'TEXT';
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

for(my $i=0; $i < scalar(@{%$res->{result}}); $i++){
    $res->{result}[$i]{'url'} = $uri->scheme . "://" . $uri->host . substr($res->{result}[$i]{'url'}, 2);
}

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
