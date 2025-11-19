# RobonAUT Server

[![Docker](https://github.com/BSStudio/robonaut-server/actions/workflows/docker.yml/badge.svg)](https://github.com/BSStudio/robonaut-server/actions/workflows/docker.yml)
[![Gradle](https://github.com/BSStudio/robonaut-server/actions/workflows/gradle.yml/badge.svg)](https://github.com/BSStudio/robonaut-server/actions/workflows/gradle.yml)
[![Integration](https://github.com/BSStudio/robonaut-server/actions/workflows/integration.yml/badge.svg)](https://github.com/BSStudio/robonaut-server/actions/workflows/integration.yml)
[![Release](https://github.com/BSStudio/robonaut-server/actions/workflows/release.yml/badge.svg)](https://github.com/BSStudio/robonaut-server/actions/workflows/release.yml)
![GitHub Release Date](https://img.shields.io/github/release-date/BSStudio/robonaut-server)
![GitHub Tag](https://img.shields.io/github/v/tag/BSStudio/robonaut-server)
![GitHub branch checks state](https://img.shields.io/github/checks-status/BSStudio/robonaut-server/main)
![Codecov branch](https://img.shields.io/codecov/c/gh/BSStudio/robonaut-server/main)
![Swagger Validator](https://img.shields.io/swagger/valid/3.0?specUrl=https%3A%2F%2Fraw.githubusercontent.com%2FBSStudio%2Frobonaut-server%2Fmain%2Fserver%2Fweb%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fopen-api.yaml)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/BSStudio/robonaut-server)
![GitHub](https://img.shields.io/github/license/BSStudio/robonaut-server)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=bugs)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=code_smells)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=ncloc)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=alert_status)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=security_rating)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=sqale_index)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_robonaut-server&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=BSStudio_robonaut-server)
![GitHub deployments](https://img.shields.io/github/deployments/BSStudio/robonaut-server/github-pages)

***
[RobonAUT](http://robonaut.aut.bme.hu/) is an annual robot competition organized by the Faculty of Electrical Engineering and Informatics of Budapest University of Technology and Economics, dating back more than a decade.
The event is livestreamed by BSS, one of the university student-operated video studios.
During the live broadcast, we wanted to show live data to our viewers.
The organizers followed the events of the races in their own software.
To display the events, we created software that uses [CasparCG](https://casparcg.com/) to display the data on the stream.

This program persisted the events of the tournament and broadcasted the details of the events to the playout software.

```mermaid
---
title: Architecture
---
graph RL
rcs["Race Control Software"]
server["Robonaut Server"]
db["MongoDB"]
playout["Playout Server"]
cg["Caspar CG"]

rcs --> server
db <--> server
server <-- Rabbit MQ --> playout
playout --> cg
```

## Run the application
### Using docker compose
```shell
docker compose up -d
```

### Using gradle
```shell
./gradlew bootRun
```

## Dependencies
The project using **MongoDB** as its persistent storage.  
For message broadcasting **RabbitMQ** is used.

## Environment variables
The required environment variables can be found here:  
`application/src/main/resources/META-INF/additional-spring-configuration-metadata.json`  
For MongoDB and RabbitMQ specific variables visit Spring Boot documentations.

## Documentation

To access the documentation run the application and navigate to `http://localhost:8080/`.
