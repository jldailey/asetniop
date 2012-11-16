#!/usr/bin/env perl

my $keys = {
	1 => "a",
	2 => "s",
	4 => "e",
	8 => "t",
	16 => "n",
	32 => "i",
	64 => "o",
	128 => "p",
	256 => "shift",
	512 => "space"
};

while ($line = <>) {
	chomp $line;
	if( $line =~ /^\s+(\d+):\s+(.*)/ ) {
		$key = $1;
		$value = $2;
		$value =~ s/# .*$//;
		$line = "\t$key: $value # ";
		for( $i = 1; $i <= 512; $i = $i << 1 ) {
			if( ($key & $i) == $i ) {
				$line = $line . $keys->{$i} . "+";
			}
		}
		$line =~ s/\+$//;
	}
	print $line . "\n";
}
