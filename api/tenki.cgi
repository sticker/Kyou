#!/usr/bin/perl

#########################################################
#
# livedoor お天気Webサービス 天気情報取得API
#
#  - 天気情報報を取得しJSON形式で出力する
#  - INパラメータ
#    city : エリアID
#
#  - OUT形式
#
#    [
#       {
#           "date":"日付(yyyy-mm-dd)",
#           "telop":"天気",
#       },
#       ・・・
#    ]
#
#########################################################

use strict;
use warnings;

use CGI;
use YAML;
use Data::Dumper;
use Web::Scraper;
use LWP::UserAgent;
use URI;
use JSON;
use Encode;

print "Content-type:text/plain; charset=UTF-8\n\n";

my $url = 'http://weather.livedoor.com/forecast/webservice/json/v1?';
my $cgi = new CGI;
$url = $url . "city=" . $cgi->param("city");

my $req = HTTP::Request->new('GET', $url);
#UserAgentを変える
use LWP::UserAgent;
my $ua = new LWP::UserAgent( agent => 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)');

#外部サイトへリクエスト
my $response = $ua->request($req);
#print Dumper $response;

#外部サイトからエラー応答が返ってきたらファイル出力せず終了
unless ($response->is_success) {
    print "ERROR:remote site is down.";
    exit 1;
}

#エンコード指定
#my ($encoding) = $response->header('Content-Type') =~ /charset=([\w\-]+)/g;
#外部サイトからの応答をScrape
#my $res = $scraper->scrape( Encode::decode($encoding, $response->content));

#print Dumper $res;
#print "===============================\n\n";

#JSONレスポンスを解析
my $result = JSON->new->utf8(0)->decode(decode_utf8($response->content));
if ($result->{Error}) {
    warn encode('utf-8', $result->{Error}{Message}), "\n";
} else {
    foreach my $key (keys %{$result}) {
        my $depth = $result->{$key};
#        print encode('utf-8', $key) . ': ' . $depth . "\n";
    }
#    print Dumper($result), "\n";
#    print "===============================\n\n";
}

#JSON形式で出力
my $json = new JSON;
#my $json_text = '[';
my $json_text = '';
$json_text .= $json->encode($result->{forecasts}).', ';
$json_text =~ s/,.$//s; #最後のカンマを除去
#$json_text .= ']';  #JSONハッシュ配列の閉じ

#print utf8::is_utf8($json_text) ? encode('utf-8', $json_text) : $json_text;

print utf8::is_utf8($json_text) ? encode('utf-8', $json_text) : $json_text;
