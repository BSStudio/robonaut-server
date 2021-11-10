plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

extra["springBootVersion"] = "2.5.6"
extra["springDependencyManagement"] = "1.0.11.RELEASE"
extra["kotlinVersion"] = "1.6.0-RC2"
extra["ktlintVersion"] = "10.2.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${property("springBootVersion")}")
    implementation("io.spring.gradle:dependency-management-plugin:${property("springDependencyManagement")}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlinVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${property("kotlinVersion")}")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:${property("ktlintVersion")}")
}
