ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rococo-geo-$VER.jar ./

EXPOSE 8096
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rococo-geo-$VER.jar
