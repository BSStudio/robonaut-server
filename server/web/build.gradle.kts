plugins {
    id("spring-module-conventions")
    id("kotlin-testing-conventions")
    id("spotless-conventions")
}

dependencies {
    api(project(":server:service"))
    implementation(libs.springdoc)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("io.projectreactor:reactor-test")
}
