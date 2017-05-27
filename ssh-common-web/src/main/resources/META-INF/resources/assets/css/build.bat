@echo off

echo.
echo ==================== build begin ====================

REM set cssIn=app.css
REM set cssOut=app.min.css

REM optimizeCss = standard | keepLines | keepComments | keepComments.keepLines | none
REM node ..\js\r.js -o cssIn=%cssIn% out=%cssOut% optimizeCss=standard

node ..\js\r.js -o build.js

echo ==================== build end ====================
echo. & pause