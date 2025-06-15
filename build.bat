@echo off
:: scala-cli fmt .
:: if %errorlevel% neq 0 (
::     echo Formatting failed!
::     exit /b %errorlevel%
:: )

scala-cli run . --native-compile -IC:\libs\SDL2\include --native-linking -lSDL2 --native-linking -LC:\libs\SDL2\lib --native-linking -lopengl32