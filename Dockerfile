ARG JDK=registry.access.redhat.com/ubi8/openjdk-11

FROM $JDK as build
# cache dependencies
COPY --chown=jboss ./gradlew                    ./
COPY --chown=jboss ./settings.gradle.kts        ./
COPY --chown=jboss ./gradle                     ./gradle/
COPY --chown=jboss ./buildSrc/build.gradle.kts  ./buildSrc/
COPY --chown=jboss ./buildSrc/src               ./buildSrc/src/
COPY --chown=jboss ./web/build.gradle.kts       ./web/
COPY --chown=jboss ./service/build.gradle.kts   ./service/
COPY --chown=jboss ./messaging/build.gradle.kts ./messaging/
COPY --chown=jboss ./data/build.gradle.kts      ./data/
COPY --chown=jboss ./app/build.gradle.kts       ./app/
RUN ./gradlew
# build
COPY --chown=jboss ./ ./
ARG BUILD_ARG="bootJar --parallel"
RUN ./gradlew $BUILD_ARG

FROM $JDK as app
ARG BOOT_JAR=/home/jboss/app/build/libs/*.jar
COPY --chown=jboss --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
