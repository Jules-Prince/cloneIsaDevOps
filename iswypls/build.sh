#!/bin/bash

echo "Compiling the NestJS Parking service"
npm install
docker build -t devopsteama/iswypls .
