import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("java-conventions")
    id("io.spring.dependency-management")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2020.0.2"

dependencyManagement {
    imports {
        // https://docs.spring.io/spring-boot/docs/2.4.5/gradle-plugin/reference/htmlsingle/#managing-dependencies-dependency-management-plugin-using-in-isolation
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
