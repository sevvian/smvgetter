// Define versions in one place for maintainability.
// The kotlin_version is now explicitly defined to match the plugin version.
val ktor_version: String by project
val logback_version: String by project
val kotlin_version = "1.9.23" // Match the version from the plugins block

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.extractor.api"
version = "1.0.0"

application {
    mainClass.set("com.extractor.api.ApplicationKt")
}

repositories {
    mavenCentral()
    // JitPack is required for some of Cloudstream's dependencies.
    maven { url = uri("https://jitpack.io") }
}

// Configure the shadowJar task to create an executable "fat" JAR.
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJarTask> {
    archiveBaseName.set("app")
    archiveClassifier.set("")
    archiveVersion.set("")
    // This is critical for the 'reflections' library to work correctly in the fat JAR.
    // It merges service descriptor files instead of overwriting them.
    mergeServiceFiles()
}

dependencies {
    // Ktor Framework
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-http-content:$ktor_version") // Required for serving static files

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Cloudstream Submodule Dependency
    implementation(project(":cloudstream:app"))

    // Reflection library used by ExtractorLogic to discover extractors at runtime.
    implementation("org.reflections:reflections:0.10.2")

    // Testing Dependencies
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    // Use the explicitly defined kotlin_version for the test dependency.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}