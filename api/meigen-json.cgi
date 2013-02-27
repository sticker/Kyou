#!/usr/bin/perl

use strict;
use warnings;

use CGI;
use DBI;
use JSON;

print "Content-type:text/plain; charset=UTF-8\n\n";

#データベース接続情報
my $data_source = 'DBI:mysql:kyou:localhost';
my $username    = 'kyou';
my $auth        = 'kyou00';

my $dbh = DBI->connect($data_source, $username, $auth);
$dbh->do("SET NAMES utf8");

#my $sql = "SELECT text, auther FROM meigen ORDER BY RAND() limit 0, 3;";
my $sql = "SELECT text, auther FROM meigen ORDER BY RAND() limit 0, 1;";
my $sth = $dbh->prepare($sql);
$sth->execute();

my $json = '['; #JSONハッシュ配列の開始
while ( my $ref = $sth->fetchrow_hashref() ){
    $json .= (JSON->new->encode($ref).', ');
}

$json =~ s/,.$//s; #最後のカンマを除去
$json .= ']';  #JSONハッシュ配列の閉じ

print $json;
