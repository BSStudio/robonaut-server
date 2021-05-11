plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("checkstyle-conventions")
    id("sonarqube-conventions")
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
}
