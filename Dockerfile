# Update gradle/gradle-wrapper.properties accordingly
ARG GRADLE=gradle:6.7.1-jdk11
ARG JDK=openjdk:11-slim

FROM $GRADLE as build
COPY . .
RUN gradle bootJar --parallel

FROM $JDK
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
COPY --from=build ./application/build/libs/app.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
