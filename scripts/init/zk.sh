#!/usr/bin/env bash
cd /opt
wget http://nexus.yijifu.net/nexus/service/local/repositories/fintech-releases/content/zk/zk/3.4.10/zk-3.4.10.zip
unzip zk-3.4.10.zip
/opt/zookeeper-3.4.10/bin/zkServer.sh start