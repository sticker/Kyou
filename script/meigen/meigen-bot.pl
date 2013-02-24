#!/usr/bin/perl

use strict;
use warnings;

use Web::Scraper;
use URI;

my $scraper = scraper {
    process 'p.js-tweet-text', 'meigen[]' => 'TEXT';
};

my $uri = new URI('https://twitter.com/positive_bot3');

my $res = $scraper->scrape($uri);

foreach (@{$res->{meigen}}){
    print $_;
}


