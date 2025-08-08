package com.cncverse

import com.cncverse.UltimaMediaProvidersUtils.ServerName
import com.cncverse.UltimaMediaProvidersUtils.commonLinkLoader
import com.cncverse.UltimaUtils.Category
import com.cncverse.UltimaUtils.LinkData
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.amap
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorLink

class AnitakuMediaProvider : MediaProvider() {
    override val name = "Anitaku"
    override val domain = "https://anitaku.bz"
    override val categories = listOf(Category.ANIME)
    private val apiUrl = "https://ajax.gogocdn.net"

    override suspend fun loadContent(
            url: String,
            data: LinkData,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
    ) {
        data.year ?: return
        val filterUrl = "$url/filter.html?keyword=${data.title}&year[]=${data.year}"
        val filterRes = app.get(filterUrl).document
        val results = filterRes.select("ul.items > li > p.name > a").map { it.attr("href") }
        results.amap {
            val subDub = if (it.contains("-dub")) "Dub" else "Sub"
            val epUrl = url.plus(it.replace("category/", "")).plus("-episode-${data.episode}")
            val epRes = app.get(epUrl).document
            epRes.select("div.anime_muti_link > ul > li").forEach {
                val link = it.selectFirst("a")?.attr("data-video") ?: return@forEach
                val serverName =
                        when (it.className()) {
                            "anime" -> ServerName.Gogo
                            "streamwish" -> ServerName.StreamWish
                            "mp4upload" -> ServerName.Mp4upload
                            "doodstream" -> ServerName.DoodStream
                            "vidhide" -> ServerName.Vidhide
                            else -> return@forEach
                        }
                commonLinkLoader(name, serverName, link, null, subDub, subtitleCallback, callback)
            }
        }
    }

    // #region - Encryption and Decryption handlers
    // #endregion - Encryption and Decryption handlers

    // #region - Data classes
    // #endregion - Data classes

}
