pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        // Jitpack is still needed for some of Cloudstream's own dependencies, like 'nicehttp'.
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "ktor-cloudstream-extractor"