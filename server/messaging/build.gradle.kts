plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("spotless-conventions")
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
}
