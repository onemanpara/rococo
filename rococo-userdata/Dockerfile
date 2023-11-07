ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rococo-userdata-$VER.jar ./

EXPOSE 8089
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rococo-userdata-$VER.jar
