#!/usr/bin/env bash

wget http://dev.mysql.com/get/mysql57-community-release-el6-8.noarch.rpm
yum  localinstall mysql57-community-release-el6-8.noarch.rpm
yum install mysql-server -y

echo 'lower_case_table_names=1' >> /etc/my.cnf