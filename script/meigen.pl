#!/usr/bin/perl

use strict;
use warnings;

use YAML;
use Data::Dumper;
use Web::Scraper;
use URI;


my $scraper = scraper {
    process 'div.meigenbox>div.text', 'meigen[]' => 'TEXT';
    process 'div.meigenbox>div.link>ul>li', 'auther[]' => 'TEXT';
};

my $uri = new URI('http://www.meigensyu.com/posts/index/page1.html');

my $res = $scraper->scrape($uri);

print Dump $res;

foreach (@{$res->{meigen}}) {
    print $_ . "\n";
}
