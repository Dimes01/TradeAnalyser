plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.34"
val testcontainersVersion = "1.20.2"
val jsonwebtokenVersion = "0.12.6"
dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor ("org.projectlombok:lombok:$lombokVersion")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("ru.tinkoff.piapi:java-sdk-core:1.26")

    implementation("org.hibernate.orm:hibernate-core:6.6.3.Final")

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")

    implementation("io.jsonwebtoken:jjwt-api:$jsonwebtokenVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jsonwebtokenVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jsonwebtokenVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    runtimeOnly("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
    }
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude("**/models/**")
            exclude("**/entities/**")
            exclude("**/dto/**")
            exclude("**/utilities/**")
            exclude("**/configurations/**")
            exclude("**/repositories/**")
            exclude("**/TradeAnalyserAuthApplication.java")
        }
    }))
}