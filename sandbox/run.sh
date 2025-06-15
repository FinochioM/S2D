#!/bin/bash

LIB_PATH="<path-to-lib>"
SLD2_LIB_PATH="<path-to-lib>"
scala-cli run . --native-compile -I$LIB_PATH --native-linking -lSDL2 --native-linking -L$SLD2_LIB_PATH --native-linking -lopengl32