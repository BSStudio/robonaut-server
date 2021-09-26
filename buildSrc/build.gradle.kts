plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

extra["springBootVersion"] = "2.5.4"
extra["springDependencyManagement"] = "1.0.11.RELEASE"

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${property("springBootVersion")}")
    implementation("io.spring.gradle:dependency-management-plugin:${property("springDependencyManagement")}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("org.jetbrains.kotlin:kotlin-allopen")
}
