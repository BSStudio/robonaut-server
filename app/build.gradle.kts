plugins {
    id("spring-app-conventions")
    id("checkstyle-conventions")
    id("sonarqube-conventions")
}

dependencies {
    api(project(":web"))
    implementation("org.springframework.boot:spring-boot-starter")
}
