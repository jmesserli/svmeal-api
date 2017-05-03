FROM openjdk:8-jre-alpine

VOLUME /tmp
ADD target/svmeal-api-*.jar springApp.jar
RUN sh -c 'touch /springApp.jar'
#ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /springApp.jar"]