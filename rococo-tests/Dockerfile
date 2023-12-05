ARG DOCKER
FROM $DOCKER
ENV JAVA_OPTS=""

WORKDIR /rococo
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY ./rococo-tests ./rococo-tests
COPY ./rococo-grpc-common ./rococo-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

RUN sed -i 's/\r$//' ./rococo-tests/send_results.sh \
    && sed -i 's/\r$//' gradlew \
    && chmod +x ./rococo-tests/send_results.sh \
    && chmod +x gradlew

CMD ./gradlew test -Dtest.env=docker; bash ./rococo-tests/send_results.sh
