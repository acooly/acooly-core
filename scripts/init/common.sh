#!/usr/bin/env bash

# use aliyun yum mirror
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-6.repo
yum clean all
yum makecache

setenforce 0
sed -i 's/=enforcing/=disabled/g' /etc/selinux/config
echo "1.selinux disabled successful."

service  iptables stop
chkconfig  iptables off

# change host name
echo "please enter a new host name:"
read -p "new host name:" -t 30 newHostName
if [ "$newHostName" = "" ]; then
  echo "new host name is empty, not set new hostname."
  exit 0
fi
hostname $newHostName
echo "sed 's/^HOSTNAME=.*/HOSTNAME=$newHostName/g' /etc/sysconfig/network"|sh
echo "127.0.0.1 $newHostName" >> /etc/hosts
echo "2.host name changed to " $newHostName

yum install lrzsz git telnet -y