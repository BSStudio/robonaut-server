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

val excluded = setOf(
    "**/model/**",
    "**/entity/**",
    "**/config/**",
    "**/configuration/**",
    "**/exception/**"
)

tasks.jacocoTestReport {
    // tests are required to run before generating the report
    dependsOn(tasks.test)
    // require xml report
    reports {
        xml.required.set(true)
    }
    // exclude excluded packages from test report
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(excluded)
                }
            }
        )
    )
}

tasks.jacocoTestCoverageVerification {
    // tests are required to run before generating the coverage verification
    dependsOn(tasks.test)
    // set required coverage to 100%
    violationRules {
        rule {
            limit {
                minimum = BigDecimal("1.00")
            }
        }
    }
    // exclude excluded packages from test coverage verification
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(excluded)
                }
            }
        )
    )
}
