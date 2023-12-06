#!/bin/bash

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

front="./rococo-client/";
front_image="onemanpara/rococo-client:latest";

FRONT_IMAGE="$front_image" docker-compose down

docker_containers="$(docker ps -a -q)"
docker_images="$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'rococo')"

if [ ! -z "$docker_containers" ]; then
  echo "### Stop containers: $docker_containers ###"
  docker stop $(docker ps -a -q)
  docker rm $(docker ps -a -q)
fi
if [ ! -z "$docker_images" ]; then
  echo "### Remove images: $docker_images ###"
  docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'rococo')
fi

echo "### Build images (front: $front) ###"
bash ./gradlew clean build dockerTagLatest -x :rococo-tests:test
cd "$front" || exit
bash ./docker-build.sh dev

cd ../
docker images
FRONT_IMAGE="$front_image" docker-compose up -d
docker ps -a
