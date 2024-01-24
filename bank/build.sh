#!/bin/bash

echo "Compiling the NestJS Bank"
npm install
docker build -t devopsteama/bank .
