package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.newExtractorLink

// Asianload is an alias for GogoCDN, but we create a separate file for clarity.
class Asianload : GogoCDN() {
    override var name = "Asianload"
    override var mainUrl = "https://asian-load.com"
}