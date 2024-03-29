FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1-16 AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./gradlew                    ./
COPY ./settings.gradle.kts        ./
COPY ./gradle                     ./gradle/
COPY ./buildSrc/src               ./buildSrc/src/
COPY ./buildSrc/*.gradle.kts      ./buildSrc/
COPY ./web/build.gradle.kts       ./web/
COPY ./service/build.gradle.kts   ./service/
COPY ./messaging/build.gradle.kts ./messaging/
COPY ./data/build.gradle.kts      ./data/
COPY ./app/build.gradle.kts       ./app/
RUN ./gradlew
# build
COPY ./ ./
ARG BUILD_ARG="bootJar --parallel"
RUN ./gradlew $BUILD_ARG

FROM bellsoft/liberica-openjre-alpine-musl:21.0.1-12 AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/app/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
