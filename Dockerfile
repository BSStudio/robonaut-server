# Update gradle/gradle-wrapper.properties accordingly
ARG GRADLE=gradle:6.8.1-jdk11
ARG JDK=openjdk:11-slim

FROM $GRADLE as build
COPY . .
RUN gradle bootJar --parallel

FROM $JDK
WORKDIR /home/spring
COPY --from=build /home/gradle/application/build/libs/app.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
