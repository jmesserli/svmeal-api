FROM maven:3-eclipse-temurin-17 as builder

COPY . /src
RUN cd /src && mvn clean package

FROM eclipse-temurin:17-alpine

VOLUME /tmp

RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Europe/Zurich /etc/localtime \
    && apk del --no-cache tzdata

COPY --from=builder /src/target/svmeal-api-*.jar springApp.jar
RUN sh -c 'touch /springApp.jar'
#ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /springApp.jar"]
