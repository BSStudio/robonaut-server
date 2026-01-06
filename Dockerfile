FROM bellsoft/liberica-runtime-container:jdk-21.0.9_12-crac-cds-musl@sha256:2b18a8ef5e494d8c5201748d5d568b9fbfdc84060ccd0962a8779c0547597ff7 AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./buildSrc/*.gradle.kts             ./buildSrc/
COPY ./buildSrc/src                      ./buildSrc/src/
COPY ./gradle                            ./gradle/
COPY ./server/build.gradle.kts           ./server/
COPY ./server/common/build.gradle.kts    ./server/common/
COPY ./server/data/build.gradle.kts      ./server/data/
COPY ./server/messaging/build.gradle.kts ./server/messaging/
COPY ./server/model/build.gradle.kts     ./server/model/
COPY ./server/service/build.gradle.kts   ./server/service/
COPY ./server/web/build.gradle.kts       ./server/web/
COPY ./gradlew                           ./
COPY ./gradle.properties                 ./
COPY ./settings.gradle.kts               ./
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew
# build
COPY ./buildSrc ./buildSrc
COPY ./server   ./server
ARG BUILD_ARG="bootJar"
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew ${BUILD_ARG}

FROM bellsoft/liberica-runtime-container:jre-25.0.1_11-cds-musl@sha256:5ac69342fe674891c7381aae9d47c2849a18b321736350084775b481644586d5 AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]

EXPOSE 8080
