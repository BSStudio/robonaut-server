plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.4.3")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
}
