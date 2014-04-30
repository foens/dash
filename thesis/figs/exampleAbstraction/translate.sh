#!/bin/bash
for f in *.dot
do
	sed "s/0 <= a/a >= 0/g" $f  | \
		sed "s/!(a >= 0)/a < 0/g" | \
		sed "s/<=/\&le;/g" | \
		sed "s/!=/\&ne;/g" | \
		sed "s/AND/\&and;/g" | \
		sed "s/>=/\&ge;/g" > $f.out
	rm $f
	mv $f.out $f
done