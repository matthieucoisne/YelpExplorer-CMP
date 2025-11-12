package cmp.yelpexplorer.core.injection

import com.apollographql.apollo.ApolloClient
import com.apollographql.ktor.ktorClient
import cmp.yelpexplorer.core.utils.Const
import cmp.yelpexplorer.core.utils.DataSource
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatcherModule = module {
    single<CoroutineDispatcher>(named(Const.DISPATCHER_IO)) { Dispatchers.IO }
}

val dataSourceModule = module {
    when (Const.DATASOURCE) {
        DataSource.REST -> {
            single { provideHttpClient(serverUrl = Const.URL_REST) }
        }
        DataSource.GRAPHQL -> {
            single { provideHttpClient(serverUrl = Const.URL_GRAPHQL) }
            single { provideApolloClient(httpClient = get()) }
        }
    }
}

// This needs to be below the definition of the 2 modules above, otherwise Koin will throw a NPE
val appModule = module {
    includes(
        dispatcherModule,
        dataSourceModule,
    )
}

private fun provideHttpClient(serverUrl: String): HttpClient {
    return httpClient {
        install(HttpTimeout) {
            socketTimeoutMillis = 15_000
            requestTimeoutMillis = 15_000
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

        defaultRequest {
            header("Authorization", "Bearer ${Const.API_KEY}")
            header("Content-Type", "application/json")
            header("Accept-Language", "en-US")

            url(serverUrl)
        }
    }
}

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

fun provideApolloClient(httpClient: HttpClient): ApolloClient {
    return ApolloClient.Builder()
        .serverUrl(Const.URL_GRAPHQL) // Required even if HttpClient has been set up with the url already.
        .ktorClient(httpClient)
        .build()
}
