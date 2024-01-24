#!/bin/bash

function build_dir()  # $1 is the dir to get it
{
    cd $1
    chmod u+x build.sh
    ./build.sh
    cd ..
}

echo "** Building all"

build_dir "cli"

build_dir "bank"

build_dir "iswypls"

build_dir "backend"

echo "** Done building all"
