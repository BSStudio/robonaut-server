plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("checkstyle-conventions")
    id("sonarqube-conventions")
}

dependencies {
    api(project(":data"))
    api(project(":messaging"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("io.projectreactor:reactor-core")
    testImplementation("io.projectreactor:reactor-test")
}
