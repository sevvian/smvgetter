// Define versions in one place for maintainability.
val ktor_version: String by project
val logback_version: String by project
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

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("app")
    archiveClassifier.set("")
    archiveVersion.set("")
    mergeServiceFiles()
}

// This block tells Gradle to include the Cloudstream source code from the submodule
// in our project's compilation. This is the key to making the extractors available.
sourceSets {
    main {
        // We need to include both the 'library' and 'repo' modules from Cloudstream.
        // 'library' contains the core APIs (ExtractorApi, ExtractorLink).
        // 'repo' contains the actual extractor implementations (DoodStream, etc.).
        // We also include 'commonMain' as Cloudstream is a multiplatform project.
        java.srcDirs(
            "Cloudstream/library/src/main/kotlin",
            "Cloudstream/library/src/commonMain/kotlin",
            "Cloudstream/repo/src/main/kotlin",
            "Cloudstream/repo/src/commonMain/kotlin"
        )
    }
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

    // == CLOUDSTREAM'S OWN DEPENDENCIES ==
    // Since we are compiling Cloudstream's code directly, we must provide
    // the libraries that its code depends on.
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    // 'nicehttp' is Cloudstream's networking library, available on Jitpack.
    implementation("com.github.LagradOst:nicehttp:2.0.3")

    // Testing Dependencies
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}