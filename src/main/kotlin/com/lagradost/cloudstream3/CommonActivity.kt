package com.lagradost.cloudstream3

/**
 * Mock for a commonly referenced object in Cloudstream.
 * We provide a nullable `activity` property that will always be null
 * on the server, which is expected by the parts of the code that use it.
 */
object CommonActivity {
    var activity: Any? = null
}