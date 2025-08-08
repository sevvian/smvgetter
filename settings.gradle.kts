rootProject.name = "ktor-cloudstream-extractor"

// Define where Gradle should look for all dependencies for the entire project.
// This is the modern, centralized approach that Gradle requires.
dependencyResolutionManagement {
    repositories {
        mavenCentral() // For Ktor, Logback, and other standard libraries.
        maven { url = uri("https://jitpack.io") } // For nicehttp and other GitHub-hosted libraries.
    }
}