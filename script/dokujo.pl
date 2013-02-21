#!/usr/bin/perl

use strict;
use warnings;

use YAML;
use Data::Dumper;
use Web::Scraper;
use URI;
use JSON;
use Data::Structure::Util qw(unbless);
use Encode;

my $scraper = scraper {
    process 'div.description', 'result[]' => scraper {
        process 'a', 'url' => [ '@href', sub { $_->as_string } ];
        process 'a>img', 'image' => [ '@src', sub{ $_->as_string } ],
                       'title' => '@title';
    };
};

my $uri = new URI('http://www.officiallyjd.com/');

my $res = $scraper->scrape($uri);

#print Dumper $res;
#print "===============================\n\n";


my $json = new JSON;

my $json_text = '[';
$json_text .= $json->encode($res->{result}).', ';
$json_text =~ s/,.$//s; #最後のカンマを除去
$json_text .= ']';  #JSONハッシュ配列の閉じ

print utf8::is_utf8($json_text) ? encode('utf-8', $json_text) : $json_text;
