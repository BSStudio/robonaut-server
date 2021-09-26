plugins {
    id("dependency-management")
    jacoco
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
    // generate report after tests run
    finalizedBy(tasks.jacocoTestReport)
}

tasks.check {
    // coverage is part of check
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestReport {
    // tests are required to run before generating the report
    dependsOn(tasks.test)
}

val excluded = setOf(
    "**/model/**",
    "**/entity/**",
    "**/config/**",
    "**/configuration/**",
    "**/exception/**"
)

tasks.jacocoTestCoverageVerification {
    // tests are required to run before generating the coverage verification
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = BigDecimal(1.00)
            }
        }
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(excluded)
            }
        })
    )
}
