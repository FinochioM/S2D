@echo off

scala-cli run . --native-compile -IC:\libs\SDL2\include --native-compile -IC:\libs\STB\include --native-compile -IC:\libs\glew\include --native-linking -lSDL2 --native-linking -LC:\libs\SDL2\lib --native-linking -LC:\libs\STB --native-linking -lstb_image --native-linking -LC:\libs\glew\lib\Release\x64 --native-linking -lglew32 --native-linking -lopengl32 --native-linking -lglu32