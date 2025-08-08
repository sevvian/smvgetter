package android.util

import org.slf4j.LoggerFactory

/**
 * A mock for the Android Log class that redirects log messages to SLF4J,
 * a standard logging facade for JVM applications. This allows us to see
 * extractor logs in the server console.
 */
object Log {
    private val logger = LoggerFactory.getLogger("CloudstreamExtractor")

    @JvmStatic
    fun d(tag: String, msg: String): Int {
        logger.debug("[$tag] $msg")
        return 0
    }

    @JvmStatic
    fun e(tag: String, msg: String): Int {
        logger.error("[$tag] $msg")
        return 0
    }
    
    @JvmStatic
    fun e(tag: String, msg: String, tr: Throwable): Int {
        logger.error("[$tag] $msg", tr)
        return 0
    }

    @JvmStatic
    fun i(tag: String, msg: String): Int {
        logger.info("[$tag] $msg")
        return 0
    }

    @JvmStatic
    fun w(tag: String, msg: String): Int {
        logger.warn("[$tag] $msg")
        return 0
    }

    @JvmStatic
    fun v(tag: String, msg: String): Int {
        logger.trace("[$tag] $msg")
        return 0
    }
}