import com.palantir.gradle.docker.DockerExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val junitVersion = "5.2.0"

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"

    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin ("plugin.jpa") version kotlinVersion

    id("com.palantir.docker") version "0.25.0"
    id("com.github.ben-manes.versions") version "0.28.0"

    `maven-publish`
}

group = "org.devshred"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core:5.2.4")
    implementation("org.postgresql:postgresql:42.2.6")

    implementation("net.javacrumbs.shedlock:shedlock-spring:4.8.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:4.8.0")
//    implementation("ch.qos.logback:logback-classic:1.2.3")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

val bootJar: BootJar by tasks

configure<DockerExtension> {
    name = "devshred/lockapp"
    tag("latest", "devshred/lockapp:latest")
    files(bootJar.archiveFile)
    setDockerfile(file("src/main/docker/Dockerfile"))
    buildArgs(mapOf("JAR_FILE" to bootJar.archiveFileName.get()))
    dependsOn(tasks["build"])
    pull(true)
}

springBoot {
    buildInfo()
}