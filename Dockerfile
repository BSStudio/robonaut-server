FROM bellsoft/liberica-runtime-container:jdk-21.0.9_12-crac-cds-musl@sha256:edf3be814fd9f27b2b7058880cbba966bd1af44b142671736a1a0e057f6a4610 AS build
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

FROM bellsoft/liberica-runtime-container:jre-25.0.1_11-cds-musl@sha256:445d46f3828e4e8683b2a19b59e4546a612d10f9ba6abbec2a01fd0ca0a2cc58 AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
