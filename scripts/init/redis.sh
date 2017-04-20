#!/usr/bin/env bash
cd /opt
yum install -y  tcl
wget http://download.redis.io/redis-stable.tar.gz
tar zxvf redis-stable.tar.gz
cd /opt/redis-stable
make
make install
sed -i 's/daemonize no/daemonize yes/g' /opt/redis-stable/redis.conf
redis-server /opt/redis-stable/redis.conf