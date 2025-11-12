package cmp.yelpexplorer.core.injection

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

actual fun httpClient(
    config: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(Darwin) {
    config(this)
}
