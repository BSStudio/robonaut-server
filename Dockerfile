FROM bellsoft/liberica-runtime-container:jdk-21.0.8_13-crac-cds-musl@sha256:0318ceabd0b357c07f8baddd657a948cbcb2930cc536353eb22c4667aeafee66 AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./buildSrc/*.gradle.kts             ./buildSrc/
COPY ./buildSrc/src                      ./buildSrc/src/
COPY ./gradle                            ./gradle/
COPY ./server/build.gradle.kts           ./server/
COPY ./server/data/build.gradle.kts      ./server/data/
COPY ./server/messaging/build.gradle.kts ./server/messaging/
COPY ./server/service/build.gradle.kts   ./server/service/
COPY ./server/web/build.gradle.kts       ./server/web/
COPY ./gradlew                           ./
COPY ./gradle.properties                 ./
COPY ./settings.gradle.kts               ./
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew
# build
COPY ./server ./server
ARG BUILD_ARG="bootJar"
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew ${BUILD_ARG}

FROM bellsoft/liberica-runtime-container:jre-25-cds-musl@sha256:f0fecc1b5626029f427be72c569ae2fb8704d8d665c110cb5075c69ab4925f37 AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
