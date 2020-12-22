# Update gradle/gradle-wrapper.properties accordingly

ARG GRADLE=gradle:6.5.0-jdk14
ARG JDK=openjdk:14-alpine

FROM $GRADLE as build
COPY . .
RUN gradle bootJar --parallel
# TODO add clean and build lifecycle

FROM $JDK
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
COPY --from=build /home/gradle/application/build/libs/app.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
