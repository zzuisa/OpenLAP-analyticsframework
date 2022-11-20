FROM openjdk:11-ea-11-jdk-slim

VOLUME /tmp

ENV mongo.database=learninglocker \
    mongo.host=mongoDB \
    mongo.port=27017

ADD target/OpenLAP-AnalyticsFramework-2.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]