@ECHO OFF

if "%1"=="" goto thesis
if "%1"=="thesis" goto thesis
if "%1"=="clean" goto clean
goto help

:help
	echo "Need to pass command to execute"
	exit /b 1

:thesis
	pdflatex thesis
	biber    thesis
	pdflatex thesis
	pdflatex thesis
	REM Optional, run pdfsizeopt:
	REM python.exe pdfsizeopt.py --use-multivalent=no thesis.pdf
	goto end

:clean:
	call:delIfExists thesis.aux
	call:delIfExists thesis.bbl
	call:delIfExists thesis.bcf
	call:delIfExists thesis.blg
	call:delIfExists thesis.log
	call:delIfExists thesis.out
	call:delIfExists thesis.run.xml
	call:delIfExists thesis.synctex.gz
	call:delIfExists thesis.aux
	call:delIfExists thesis.toc
	del /s *.pdf
	EXIT /B 0

:end
EXIT /B

:delIfExists
IF EXIST %~1 (
	echo Deleting %~1
	del %~1
)
goto:eof