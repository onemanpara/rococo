ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rococo-museum-$VER.jar ./

EXPOSE 8093
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rococo-museum-$VER.jar
