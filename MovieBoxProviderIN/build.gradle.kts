// use an integer for version numbers
version = 1

android {
    buildFeatures {
        buildConfig = true
    }
}

cloudstream {
    language = "ta"
    // All of these properties are optional, you can safely remove them

    description = "Multi Language Movies and Series Provider (IN)"
    authors = listOf("NivinCNC")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1 // will be 3 if unspecified
    tvTypes = listOf(
        "Movie",
        "TvSeries"
    )

    iconUrl = "https://github.com/NivinCNC/CNCVerse-Cloud-Stream-Extension/raw/refs/heads/master/MovieBoxProvider/icon.png"
}
