@echo off

call:delIfExists *.eps
call:delIfExists *.pdf
call:delIfExists *.svg

for %%f in (without\*.dot) do call:dot without\%%~nf
for %%f in (with\*.dot) do call:dot with\%%~nf

EXIT /B

:delIfExists
IF EXIST without\%~1 (
	echo Deleting without\%~1
	del without\%~1
)
IF EXIST with\%~1 (
	echo Deleting with\%~1
	del with\%~1
)
goto:eof

:dot    - here starts a function identified by it`s label
REM ==== Find inkscape path
set inkscape="%HOMEPATH%\Desktop\inkscape\inkscape.exe"
set jacobInkscape="C:\Program Files (x86)\Inkscape\inkscape.exe"
if exist %jacobInkscape% set inkscape=%jacobInkscape%

REM ==== Output to SVG format with graphviz
set format=svg
set in=%~1.dot
set out=%~1.%format%
echo Translating %in% to %format%
"../../../program/dot/dot.exe" -T%format% %in% > %out%

REM ==== Convert SVG to EPS with inkscape
%inkscape% inkscape -f %out% -E %~1.eps.withcreationdate

REM ==== Remove SVG file
del %out%

REM ==== Remove creation date comment
findstr /v /b /c:"%%%%CreationDate" %~1.eps.withcreationdate > %~1.eps
del %~1.eps.withcreationdate

goto:eof