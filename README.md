# Cloudstream Extractor API Service

This project is a Ktor-based web service that exposes the powerful extractor logic from the Cloudstream Android application as a standalone API.

It uses a Git submodule to pull in the Cloudstream source code and a custom Android shim layer to allow the extractor code to compile and run in a standard JVM/server environment.

## Features

-   **Dynamic Extractor Loading**: Discovers all available Cloudstream extractors at runtime using reflection.
-   **Simple REST API**: A single `/extract` endpoint to get stream links from a supported URL.
-   **Static Frontend**: A basic web interface for easy testing.
-   **Dockerized**: Comes with a `Dockerfile` for easy containerization and deployment.
-   **Automated CI/CD**: A GitHub Actions workflow automatically builds and pushes a Docker image to the GitHub Container Registry on every push to `main`.

## API Usage

-   **Endpoint**: `GET /extract`
-   **Query Parameter**: `url` (The URL of the video page you want to extract from)
-   **Example**: `GET /extract?url=https://dood.watch/e/xxxxxxxxxx`

### Success Response (`200 OK`)

```json
[
  {
    "name": "DoodStream",
    "source": "DoodStream",
    "quality": 1,
    "url": "https://...",
    "isM3u8": false,
    "headers": {
      "Referer": "https://dood.watch/"
    }
  }
]
```

### Error Response (`404 Not Found`)

If no links can be found, the API will return a 404 status with a simple message.