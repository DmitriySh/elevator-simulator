#!/bin/sh

java -jar -Dfile.encoding=UTF-8 ../build/libs/elevator-simulator-all-*.jar -n 10 -h 5 -v 2 -d 20
