val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val shadowVersion: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.extractor"
version = "0.0.1"

application {
    mainClass.set("com.extractor.api.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Cloudstream dependencies (as they are not on Maven Central)
    // These are needed for the extractor logic to compile.
    implementation("com.github.recloudstream:nicehttp:2.0.3") // ✅ Confirmed
    implementation("org.jsoup:jsoup:1.15.3") // ✅ Confirmed
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3") // ✅ Confirmed

    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}