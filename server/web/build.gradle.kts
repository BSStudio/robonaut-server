plugins {
  id("hu.bsstudio.gradle.java-library-convention")
  id("hu.bsstudio.gradle.dependency-management")
  id("hu.bsstudio.gradle.kotlin-convention")
  id("hu.bsstudio.gradle.spotless-convention")
  id("hu.bsstudio.gradle.test-convention")
  id("hu.bsstudio.gradle.jacoco-convention")
  id("hu.bsstudio.gradle.detekt-convention")
}

dependencies {
  api(project(":server:service"))
  implementation(libs.springdoc)
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  testImplementation("io.projectreactor:reactor-test")
}
