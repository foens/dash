@echo off

call:delIfExists *.eps
call:delIfExists *.pdf
call:delIfExists *.svg

for %%f in (*.dot) do call:dot %%~nf

EXIT /B

:delIfExists
IF EXIST %~1 (
	echo Deleting %~1
	del %~1
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
%inkscape% inkscape -f %out% -E %~1.eps

REM ==== Remove SVG file
del %out%

REM ==== Remove creation date comment
rename %~1.eps %~1.eps.old
findstr /v /b /c:"%%%%CreationDate" %~1.eps.old > %~1.eps
del %~1.eps.old

goto:eof