#!/usr/bin/env bash
cd /opt
wget http://nexus.yijifu.net/nexus/service/local/repositories/fintech-releases/content/jdk8/jdk8/8u112/jdk8-8u112-x64.tar.gz
tar zxvf jdk8-8u112-x64.tar.gz
echo 'JAVA_HOME=/opt/jdk1.8.0_112' >> /etc/profile
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> /etc/profile
source /etc/profile