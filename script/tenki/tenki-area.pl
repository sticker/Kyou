#!/usr/bin/perl

#########################################################
#
# livedoor お天気Webサービス エリア情報取得スクリプト
#
#  - エリア情報xmlを取得しJSON形式で出力する
#  - 形式は以下のとおり
#
#    [
#       {
#           "pref":"県名",
#           "city[]":"地域名の配列",
#           "id[]":"地域IDの配列"
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

my $url = 'http://weather.livedoor.com/forecast/rss/primary_area.xml';
my $output = "/var/www/html/Kyou/tenki-area.json";

#Scrape設定
my $scraper = scraper {
    #process 'pref', 'pref[]' => '@title', 'city[]' => scraper {
    #    process 'city', 'city' => '@title', 'id' => '@id';
    process 'pref', 'result[]' => scraper {
        process 'pref', 'pref' => '@title';
        process 'city', 'city[]' => '@title', 'id[]' => '@id';
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

print Dumper $res;
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
