#!/bin/bash

# shellcheck disable=SC2164
cd ./api-gateway
# Step 1: Build the jar file using Gradle
./gradlew clean build

docker build -t mike272/api-gateway .

docker push mike272/api-gateway
cd ..