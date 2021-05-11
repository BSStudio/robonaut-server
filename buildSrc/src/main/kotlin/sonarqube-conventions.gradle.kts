plugins {
    id("org.sonarqube")
}

sonarqube {
    properties {
        property("sonar.projectKey", "BSStudio_robonaut-server")
        property("sonar.organization", "bsstudio")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}