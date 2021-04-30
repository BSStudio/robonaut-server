plugins {
    id("spring-app-conventions")
    id("checkstyle-conventions")
}

dependencies {
    api(project(":web"))
    implementation("org.springframework.boot:spring-boot-starter")
}
