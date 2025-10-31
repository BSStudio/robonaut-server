FROM bellsoft/liberica-runtime-container:jdk-21.0.9_12-crac-cds-musl@sha256:28879749f5981883858d1d5a1413a070b082d8c78e55a204b9f2df66ab9f1dfe AS build
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

FROM bellsoft/liberica-runtime-container:jre-25.0.1_11-cds-musl@sha256:d02bbc8a59d56de8215f5ee292d8d10110b3c38bd3562aff66643caca1aa0996 AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
