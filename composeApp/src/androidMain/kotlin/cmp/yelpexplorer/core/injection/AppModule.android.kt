package cmp.yelpexplorer.core.injection

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

actual fun httpClient(
    config: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(CIO) {
    config(this)
}
