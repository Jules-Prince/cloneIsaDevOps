#!/bin/bash

echo "Compiling the TCF Spring CLI within a multi-stage docker build"
mvn clean package
docker build --build-arg JAR_FILE=target/cli-2.0.jar -t devopsteama/cli .
