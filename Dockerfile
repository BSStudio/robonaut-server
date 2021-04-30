ARG JDK=openjdk:11-slim

FROM $JDK as build
# cache dependencies
COPY ./gradlew ./settings.gradle ./build.gradle ./
COPY ./gradle                   ./gradle
COPY ./web/build.gradle         ./web
COPY ./service/build.gradle     ./service
COPY ./messaging/build.gradle   ./messaging
COPY ./data/build.gradle        ./data
COPY ./application/build.gradle ./application
RUN ./gradlew
# build
COPY . .
ARG BUILD_ARG="bootJar --parallel"
RUN ./gradlew $BUILD_ARG

FROM $JDK as app
USER spring
WORKDIR /home/spring
ARG BOOT_JAR=/application/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
