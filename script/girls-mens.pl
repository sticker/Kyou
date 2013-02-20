#!/usr/bin/perl

use strict;
use warnings;

use YAML;
use Data::Dumper;
use Web::Scraper;
use URI;
use JSON;

my $scraper = scraper {
    process 'div.mainPhoto>p>a>img', 'photo[]' => '@src';
    process 'div.mainPhoto>p>a', 'girls_link[]' => '@href';
};

my $uri = new URI('http://www.oricon.co.jp/trend/');

my $res = $scraper->scrape($uri);

#print Dump $res;

my $json = '[';
$json .= (JSON->new->encode($res).', ');

$json =~ s/,.$//s; #最後のカンマを除去
$json .= ']';  #JSONハッシュ配列の閉じ

print $json;
