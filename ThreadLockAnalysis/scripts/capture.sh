#!/bin/bash

pid=$(jcmd | grep $1 | awk '{print $1}')          # required
dir=$2
count=${3:-10}  # defaults to 10 times
delay=${4:-0.5} # defaults to 0.5 seconds
rm -rf ../src/main/resources/$dir
mkdir ../src/main/resources/$dir
while [ $count -gt 0 ]
do
	jcmd $pid Thread.print > ../src/main/resources/$dir/$dir.$(date +%s)
	sleep $delay
	let count--
	echo -n "."
done
