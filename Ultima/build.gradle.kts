@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.konan.properties.Properties

dependencies {
    implementation("com.google.android.material:material:1.12.0")

    // FIXME remove this when crossplatform is fully supported
    val cloudstream by configurations
    cloudstream("com.lagradost:cloudstream3:pre-release")
}
// use an integer for version numbers
version = 1


cloudstream {
    // All of these properties are optional, you can safely remove them

    description = "The ultimate All-in-One home screen to access all of your extensions at one place (You need to select/deselect sections in Ultima's settings to load other extensions on home screen)"
    authors = listOf("RowdyRushya, NivinCNC")

    /**
    * Status int as the following:
    * 0: Down
    * 1: Ok
    * 2: Slow
    * 3: Beta only
    * */
    status = 1

    tvTypes = listOf("All")

    requiresResources = true
    language = "ta"

    // random cc logo i found
    iconUrl = "https://github.com/NivinCNC/CNCVerse-Cloud-Stream-Extension/raw/refs/heads/master/Ultima/icon.png"

    isCrossPlatform = false
}

android {
    buildFeatures {
        buildConfig = true
    }
}