plugins {
    id("spring-app-conventions")
    id("spotless-conventions")
}

dependencies {
    api(project(":web"))
    implementation("org.springframework.boot:spring-boot-starter")
}
