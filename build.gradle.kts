// Define versions in one place for maintainability.
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

// Add a sourceSets block to include the Cloudstream submodule's source code.
// This allows us to compile the extractor logic directly into our application,
// bypassing the Android-specific build system of the submodule.
sourceSets {
    main {
        java.srcDirs("$projectDir/cloudstream/app/src/main/java")
        kotlin.srcDirs("$projectDir/cloudstream/app/src/main/kotlin")
    }
}

// Configure the shadowJar task.
tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("app")
    archiveClassifier.set("")
    archiveVersion.set("")
    mergeServiceFiles()
}

dependencies {
    // Ktor Framework (with corrected -jvm suffixes)
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-http-content-jvm:$ktor_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Dependencies required by the Cloudstream extractor source code
    implementation("com.lagradost:nicehttp:1.1.5") // For networking
    implementation("org.jsoup:jsoup:1.17.2")      // For HTML parsing

    // Reflection library used by ExtractorLogic to discover extractors at runtime.
    implementation("org.reflections:reflections:0.10.2")

    // Testing Dependencies
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}