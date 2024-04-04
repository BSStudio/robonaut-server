FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1-16 AS build
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

FROM bellsoft/liberica-openjre-alpine-musl:21.0.1-12 AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
