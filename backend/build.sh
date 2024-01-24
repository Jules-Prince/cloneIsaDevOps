#!/bin/bash

echo "Compiling the TCF Spring BACKEND within a multi-stage docker build"
mvn clean package
docker build --build-arg JAR_FILE=target/backend-2.0.jar -t devopsteama/backend .
