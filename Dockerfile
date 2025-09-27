FROM bellsoft/liberica-runtime-container:jdk-21.0.8_13-crac-cds-musl@sha256:2cc50469908e34f6b957516f1e89928b9aeed83b978fe48a89f8c62456f53db0 AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./gradlew                           ./
COPY ./settings.gradle.kts               ./
COPY ./gradle.properties                 ./
COPY ./gradle                            ./gradle/
COPY ./buildSrc/src                      ./buildSrc/src/
COPY ./buildSrc/build.gradle.kts         ./buildSrc/
COPY ./buildSrc/settings.gradle.kts      ./buildSrc/
COPY ./server/web/build.gradle.kts       ./server/web/
COPY ./server/service/build.gradle.kts   ./server/service/
COPY ./server/messaging/build.gradle.kts ./server/messaging/
COPY ./server/data/build.gradle.kts      ./server/data/
COPY ./server/build.gradle.kts           ./server/
RUN ./gradlew
# build
COPY ./server ./server
RUN ./gradlew bootJar

FROM bellsoft/liberica-runtime-container:jre-21.0.8_12-cds-musl@sha256:5118fc630708d6b4ed3dc22cae635905ec58d961a3bc946cb8189458da1607ee AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
LABEL org.opencontainers.image.source="https://github.com/BSStudio/robonaut-server"
LABEL org.opencontainers.image.description="BSS Robonaut Server"
LABEL org.opencontainers.image.licenses="GPL-3.0"
