// Define versions in one place for maintainability.
val ktor_version: String by project
val logback_version: String by project
// Aligning Kotlin version with the upstream repository for compatibility.
val kotlin_version = "1.9.23" 

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.11"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.extractor.api"
version = "1.0.0"

application {
    mainClass.set("com.extractor.api.ApplicationKt")
}

// Define the repositories where Gradle should look for dependencies.
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") } // Jitpack is needed for Cloudstream
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("app")
    archiveClassifier.set("")
    archiveVersion.set("")
    mergeServiceFiles()
}

dependencies {
    // Ktor Framework (for our server)
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // == CLOUDSTREAM DEPENDENCIES ==
    // We now pull the Cloudstream library and extractors as pre-compiled
    // artifacts from Jitpack. This is much more stable than compiling from source.
    // Using a specific commit hash for reproducibility.
    val cloudstreamCommit = "d9131e29692a1809312a93433329a334a24765a4" 
    implementation("com.github.recloudstream.cloudstream:library:$cloudstreamCommit")
    implementation("com.github.recloudstream.cloudstream:repo:$cloudstreamCommit")

    // Testing Dependencies
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}