@echo off
title Deployment Project
echo [ACOOLY] Deploy module with pom.xml to local and remote repository.

set curdir=%~dp0
set partition=%curdir:~0,1%
%partition%:
cd %curdir%

rem if not exist pom.xml in current directory , then 'cd' parent directory
if not exist pom.xml cd..

set module_home=%cd%
echo [ACOOLY] The module location: %module_home%
echo.

set profile=
set /P profile=maven profile(def: acooly[yiji_dev,yiji_online]): %=%
if defined profile (set profile=%profile%) else set profile=acooly
echo [INFO] Chose maven profile: %profile%

call mvn clean deploy -P%profile% -Dmaven.test.skip=true -Dopt=deploy

echo.
echo [ACOOLY] deploy finished
pause