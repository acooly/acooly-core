@echo off
title Installation Project
echo [ACOOLY] Install module with pom.xml to local repository.
echo [ACOOLY] The installation module will only be released to local repository. 

cd %~dp0
rem if not exist pom.xml in current directory , then 'cd' parent directory
if not exist pom.xml cd..
set module_home=%cd%
echo [ACOOLY] The module location: %module_home%
echo.

call mvn clean install -Pdeploy -Dmaven.test.skip=true

echo.
echo [ACOOLY] Install finished
pause