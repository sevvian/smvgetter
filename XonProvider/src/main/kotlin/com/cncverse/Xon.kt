package com.cncverse

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class Xon : Plugin() {
    override fun load(context: Context) {
        val provider = XonProvider()
        registerMainAPI(provider)
    }
}
