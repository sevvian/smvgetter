package com.lagradost.cloudstream3.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A server-side (JVM) implementation of Cloudstream's coroutine helper.
 * It launches a new coroutine in the IO thread pool, which is appropriate
 * for server tasks like network requests.
 */
fun <T> ioSafe(block: suspend CoroutineScope.() -> T) {
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}