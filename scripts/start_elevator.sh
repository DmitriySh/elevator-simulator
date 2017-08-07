#!/bin/sh

java -jar -Dfile.encoding=UTF-8 ../build/libs/elevator-simulator-all-*.jar -n 15 -h 4 -v 2 -d 5
