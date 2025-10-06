FROM bellsoft/liberica-runtime-container:jdk-21.0.8_13-crac-cds-musl@sha256:159105470d7140b5318d7d85b9413e099762bbabc451a96365b885107bf8e3e0 AS build
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

FROM bellsoft/liberica-runtime-container:jre-25-cds-musl@sha256:dfba02c9de055cd89f1c3e860425bef21cd88129a49a874e6024b953d9060d05 AS app
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
