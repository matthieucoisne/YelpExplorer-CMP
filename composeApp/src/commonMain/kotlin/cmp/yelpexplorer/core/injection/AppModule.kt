package cmp.yelpexplorer.core.injection

import com.apollographql.apollo.ApolloClient
import com.apollographql.ktor.ktorClient
import cmp.yelpexplorer.core.utils.Const
import cmp.yelpexplorer.core.utils.DataSource
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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

expect fun provideHttpClient(serverUrl: String): HttpClient

fun provideApolloClient(httpClient: HttpClient): ApolloClient {
    return ApolloClient.Builder()
        .serverUrl(Const.URL_GRAPHQL) // Required even if HttpClient has been set up with the url already.
        .ktorClient(httpClient)
        .build()
}
