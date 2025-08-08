# AI Development Rules

This document outlines the technical stack and development guidelines for this Cloudstream plugin repository. Adhering to these rules ensures consistency, maintainability, and compatibility with the Cloudstream ecosystem.

## Tech Stack

The project is a collection of plugins for the Cloudstream 3 Android application. The core technologies used are:

-   **Primary Language**: Kotlin. All new code must be written in Kotlin.
-   **Platform**: Android, specifically as a Cloudstream 3 plugin.
-   **Build Tool**: Gradle with the Kotlin DSL (`.gradle.kts`).
-   **Core Framework**: The Cloudstream 3 Plugin API is the foundation for all providers.
-   **HTTP Client**: OkHttp3 is used for networking, accessed through the `nicehttp` wrapper provided by Cloudstream.
-   **HTML Parsing**: Jsoup is the standard library for all web scraping and HTML document parsing.
-   **JSON Handling**: Jackson (`jackson-module-kotlin`) is used for parsing and serializing all JSON data.
-   **Asynchronous Programming**: Kotlin Coroutines are used for all asynchronous operations, especially network requests.
-   **Cryptography**: Standard Java Cryptography Architecture (`javax.crypto`) is used for handling encrypted API responses when necessary.

## Library and Development Rules

### 1. Networking
-   **Rule**: For all standard HTTP requests, you **MUST** use the pre-configured `app` instance (`com.lagradost.nicehttp.Requests`). Do not initialize a new `OkHttpClient`.
-   **Reason**: This ensures that all requests share consistent headers (like `User-Agent`), timeout settings, and response parsing logic provided by the Cloudstream framework.

### 2. Data Parsing
-   **JSON**:
    -   **Rule**: **MUST** use the provided utility functions like `app.get(...).parsed<T>()` or `parseJson<T>()` which utilize the project's shared Jackson `ObjectMapper`.
    -   **Rule**: **MUST** define a Kotlin `data class` for every expected JSON response structure to ensure type safety.
-   **HTML**:
    -   **Rule**: **MUST** use Jsoup for all HTML parsing. Use CSS selectors (`.select()`, `.selectFirst()`) for data extraction.

### 3. Cloudstream API Usage
-   **Rule**: **MUST** use the helper functions provided by the Cloudstream 3 API to construct data models (e.g., `newMovieSearchResponse`, `newTvSeriesLoadResponse`, `newEpisode`, `newExtractorLink`).
-   **Reason**: These helpers guarantee that the data objects are correctly formatted for the main application, preventing crashes and ensuring proper UI display.

### 4. Asynchronous Code
-   **Rule**: All functions performing network or disk I/O operations **MUST** be `suspend` functions.
-   **Rule**: Use `amap` for performing concurrent network requests on a collection when requests are independent of each other.
-   **Reason**: This is critical for preventing the app from freezing and ensuring a smooth user experience, in line with modern Android development practices.

### 5. Dependencies
-   **Rule**: **DO NOT** add new third-party libraries or dependencies to the `build.gradle.kts` files.
-   **Reason**: The Cloudstream plugin environment provides all the necessary tools (OkHttp, Jackson, Jsoup). Adding external dependencies can increase plugin size and cause conflicts with the main application.

### 6. Code Structure
-   **Rule**: Keep provider logic self-contained within its own package.
-   **Rule**: Define data classes for API responses in dedicated files or within the provider file if they are small and specific to that provider.
-   **Rule**: Extract complex, reusable logic (like decryption methods) into utility objects or files if it's shared across multiple providers.