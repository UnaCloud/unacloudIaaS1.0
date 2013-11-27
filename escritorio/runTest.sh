#!/bin/bash
cd /tmp
echo hola > holap.txt
mv holap.txt /unacloud/cluster/test.ttxt
cd /unacloud/cluster
echo hola mundo > `hostname`.txt
