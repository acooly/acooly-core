@echo off
set curdir=%~dp0
set partition=%curdir:~0,1%
%partition%:
cd %curdir%
rem if not exist pom.xml in current directory , then 'cd' parent directory
if not exist pom.xml cd..

set module_home=%cd%
echo [ACOOLY] Project home location: %module_home%

set PROJECT_LIB_DIR=%module_home%\src\main\webapp\WEB-INF\lib
set PROJECT_CLASS_DIR=%module_home%\src\main\webapp\WEB-INF\classes
echo [ACOOLY] Project libraries location: %PROJECT_LIB_DIR%
echo [ACOOLY] Project classes location: %PROJECT_CLASS_DIR%
echo.

echo [ACOOLY] 1.Clean Maven Project
call mvn clean

echo [ACOOLY] 2.Clean maven-eclipse Project
call mvn eclipse:clean

echo [ACOOLY] 3.To build maven-eclipse project
del /f/s/q %PROJECT_CLASS_DIR%\*
rd /s /q %PROJECT_CLASS_DIR%
md %PROJECT_CLASS_DIR%
call mvn eclipse:eclipse -DdownloadSources=false -DoutputDirectory=%PROJECT_CLASS_DIR%

echo [ACOOLY] 4.To build and copy Project dependencies
rd /s /q %PROJECT_LIB_DIR%
md %PROJECT_LIB_DIR%
call mvn dependency:copy-dependencies -DoutputDirectory=%PROJECT_LIB_DIR%

del /f/s/q %PROJECT_LIB_DIR%\jsp-api*

echo [ACOOLY] Generate maven-eclipse project finished.
pause
