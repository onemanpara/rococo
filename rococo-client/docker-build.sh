#!/bin/bash
source ./docker.properties

echo '### Build frontend image ###'
docker build --build-arg NPM_COMMAND=${BUILD} -t ${IMAGE_NAME}:${VERSION} -t ${IMAGE_NAME}:latest .

if [ "$2" = "push" ]; then
  echo '### Push frontend image ###'
  docker push ${IMAGE_NAME}:${VERSION}
  docker push ${IMAGE_NAME}:latest
fi
