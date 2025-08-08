// Enable the plugin management block to configure where Gradle itself finds plugins.
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

// Centralize dependency repository configuration for the entire project.
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        // JitPack is required for Cloudstream's dependencies (like nicehttp).
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "cloudstream-extractor-api"

// We no longer include the submodule as a Gradle project.
// Instead, we will include its source files directly in our main project's source set.
// include(":cloudstream:app") // This line is intentionally removed.