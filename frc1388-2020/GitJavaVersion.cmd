@ECHO OFF
REM
REM This script will create a Java class which contains git build info

set "PATH=%PATH%;C:\Program Files\Git\bin;%LOCALAPPDATA%\Programs\Git\bin"

set TEMPFILE=%TEMP%\%~n0.txt

set GIT_VERSION=???????
git log -1 --pretty=format:%%h > %TEMPFILE%
set /p GIT_VERSION=<%TEMPFILE%

set GIT_BRANCH=(no branch)
git symbolic-ref --short HEAD > %TEMPFILE%
set /p GIT_BRANCH=<%TEMPFILE%

set GIT_STATUS=
git diff --shortstat HEAD > %TEMPFILE%
set /p GIT_STATUS=<%TEMPFILE%

date /t > %TEMPFILE%
set /p BUILD_DATE=<%TEMPFILE%

time /t > %TEMPFILE%
set /p BUILD_TIME=<%TEMPFILE%

echo package frc.robot;
echo public class BuildInfo {
echo   public static final String GIT_VERSION = "%GIT_VERSION%";
echo   public static final String GIT_BRANCH = "%GIT_BRANCH%";
echo   public static final String GIT_STATUS = "%GIT_STATUS%";
echo   public static final String BUILD_DATE = "%BUILD_DATE%";
echo   public static final String BUILD_TIME = "%BUILD_TIME%";
echo }
