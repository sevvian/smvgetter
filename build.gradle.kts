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
    google() // For AndroidX libraries like core-ktx
    maven { url = uri("https://jitpack.io") }
}

// Point to the 'library' module's source code, where the extractors reside.
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
    // Ktor Framework (for our server)
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Reflection library used by ExtractorLogic
    implementation("org.reflections:reflections:0.10.2")

    // == CLOUDSTREAM LIBRARY DEPENDENCIES ==
    // These are the dependencies required by the Cloudstream source code itself.
    
    // Core Android KTX library (provides common extensions)
    implementation("androidx.core:core-ktx:1.13.1")

    // HTTP Client used by Cloudstream
    implementation("com.github.Blatzar:NiceHttp:0.4.13")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktor_version")

    // Data Parsers used by Cloudstream
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Coroutines Library
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // Testing Dependencies
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}