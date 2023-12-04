ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rococo-artist-$VER.jar ./

EXPOSE 8092
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rococo-artist-$VER.jar
