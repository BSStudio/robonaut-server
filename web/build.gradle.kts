plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("checkstyle-conventions")
    id("sonarqube-conventions")
}

dependencies {
    api(project(":service"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:${property("springDocVersion")}")
    testImplementation("io.projectreactor:reactor-test")
}
