#!/usr/bin/perl

use strict;
use warnings;

use YAML;
use Data::Dumper;
use Web::Scraper;
use URI;
use JSON;

my $scraper = scraper {
    process 'div.description>a>img', 'photo[]' => '@src';
    process 'div.description>a>img', 'title[]' => '@title';
    process 'div.description>a', 'url[]' => '@href';
};

my $uri = new URI('http://www.officiallyjd.com/');

my $res = $scraper->scrape($uri);

print Dump $res;

#my $json = '[';
#$json .= (JSON->new->encode($res).', ');

#$json =~ s/,.$//s; #最後のカンマを除去
#$json .= ']';  #JSONハッシュ配列の閉じ

#print $json;
