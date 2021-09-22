plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("checkstyle-conventions")
}

dependencies {
    api(project(":service"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation(libs.springdoc)
    testImplementation("io.projectreactor:reactor-test")
}
