plugins {
    id("spring-module-conventions")
    id("kotlin-testing-conventions")
    id("spotless-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
}
