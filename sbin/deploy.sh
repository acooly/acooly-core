#!/bin/bash
# anto deploy project to nexus

curdir=`dirname $0`
cd $curdir

if [ ! -f "$curdir/pom.xml" ]; then
  cd ..
  curdir=`pwd`
fi

echo [INFO] project home : $curdir

read -p "maven profile(def: acooly[yiji_dev,yiji_online]):" -t 30 profile
if [ "$profile" == "" ]; then 
	profile=acooly 
fi
echo [INFO] profile: $profile
mvn clean deploy -P$profile -Dmaven.test.skip=true -Dopt=deploy

echo [INFO] deploy finished

read -n1 -p "Press any key to continue..."