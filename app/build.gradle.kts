plugins {
    id("spring-app-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":web"))
    implementation("org.springframework.boot:spring-boot-starter")
}
