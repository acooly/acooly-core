#!/usr/bin/env bash
cd /opt
wget http://nexus.yijifu.net/nexus/service/local/repositories/fintech-releases/content/maven/maven/3.5.0/maven-3.5.0-bin.zip
unzip maven-3.5.0-bin.zip

echo 'M2_HOME=/opt/apache-maven-3.5.0' >> /etc/profile
echo 'export PATH=$PATH:$M2_HOME/bin' >> /etc/profile
source /etc/profile