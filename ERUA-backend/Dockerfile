FROM maven as build
COPY . .
RUN  mvn -B package --file pom.xml

FROM adoptopenjdk:16-jre-hotspot

RUN mkdir /data
VOLUME /data
COPY --from=build target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]