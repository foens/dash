@echo off


call:dot exampleAbstraction

EXIT /B





:dot    - here starts a function identified by it`s label
set inkscape="%HOMEPATH%\Desktop\inkscape\inkscape.exe"
set format=svg
set in=%~1.dot
set out=%~1.%format%
echo Translating %in% to %format%
"../../../program/dot/dot.exe" -T%format% %in% > %out%
%inkscape% inkscape -f %out% -E %~1.eps
del %out%
goto:eof