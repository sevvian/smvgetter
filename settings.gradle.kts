// Enable the plugin management block to configure where Gradle itself finds plugins.
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

// Centralize dependency repository configuration for the entire project.
// This resolves the build error and is a modern Gradle best practice.
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        // JitPack is required for some of Cloudstream's dependencies.
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "cloudstream-extractor-api"

// Explicitly include the submodule in the build.
include(":cloudstream:app")