@echo off
SET path_sdk=C:\Users\BIGMAN\AppData\Local\Android\sdk\platform-tools
set mypath=%cd%
rem @echo %mypath%
rem echo %path_sdk%
cd /d %path_sdk%
rem echo %1
IF "%1"=="" goto exit
adb kill-server
adb tcpip 5555
adb connect 192.168.1.105:5555
:exit
cd /d %mypath%
rem pause