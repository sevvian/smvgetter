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

// CORRECTED: Point to the 'library' module's source code, where the extractors reside.
// The commonMain and jvmMain source sets contain all the necessary logic.
sourceSets {
    main {
        kotlin.srcDirs(
            "$projectDir/cloudstream/library/src/commonMain/kotlin",
            "$projectDir/cloudstream/library/src/jvmMain/kotlin"
        )
        java.srcDirs(
            "$projectDir/cloudstream/library/src/commonMain/java",
            "$projectDir/cloudstream/library/src/jvmMain/java"
        )
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("app")
    archiveClassifier.set("")
    archiveVersion.set("")
    mergeServiceFiles()
}

dependencies {
    // Ktor Framework
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // == CLOUDSTREAM LIBRARY DEPENDENCIES ==
    // All versions and modules are now aligned with the provided cloudstream-master/gradle/libs.versions.toml
    
    // HTTP Client (Corrected based on your research and verified in the repo)
    implementation("com.github.Blatzar:NiceHttp:0.4.13")

    // HTML Parser
    implementation("org.jsoup:jsoup:1.15.3")

    // JSON Parser (This was missing)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    // Coroutines Library (This was missing)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // Reflection library used by ExtractorLogic to discover extractors at runtime.
    implementation("org.reflections:reflections:0.10.2")

    // Testing Dependencies
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}