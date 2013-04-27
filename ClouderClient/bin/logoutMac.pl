#!/usr/bin/perl

$pid = `ps -Ajc | grep loginwindow`;
chomp $pid;
@linea = split(/\s+/, $pid);
$proc=$linea[1];
chomp $proc;
`kill $proc`;
