plugins {
    id("spring-module-conventions")
    id("kotlin-testing-conventions")
    id("spotless-conventions")
}

dependencies {
    api(project(":server:data"))
    api(project(":server:messaging"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("io.projectreactor:reactor-core")
    testImplementation("io.projectreactor:reactor-test")
}
