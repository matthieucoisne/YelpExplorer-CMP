package cmp.yelpexplorer.features.business.data.rest.repository

import app.cash.turbine.test
import cmp.yelpexplorer.core.utils.DateTimeFormater
import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSourceImpl
import cmp.yelpexplorer.features.business.data.rest.mapper.BusinessRestMapper
import cmp.yelpexplorer.features.business.data.rest.mapper.ReviewRestMapper
import cmp.yelpexplorer.utils.FileUtils
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessRestRepositoryTest : BusinessRepositoryTest() {

    @Test
    fun `get business list success`() = runTest {
        // ARRANGE
        val httpClientEngine = MockEngine.Queue().apply {
            enqueue {
                respond(
                    status = HttpStatusCode.OK,
                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                    content = FileUtils.getStringFromPath(filePath = "responses/rest/getBusinessList.json"),
                )
            }
        }
        val repository = BusinessRestRepository(
            businessRestDataSource = BusinessRestDataSourceImpl(getFakeHttpClient(httpClientEngine)),
            businessRestMapper = BusinessRestMapper(DateTimeFormater()),
            reviewRestMapper = ReviewRestMapper(DateTimeFormater()),
            ioDispatcher = UnconfinedTestDispatcher(),
        )

        // ACT
        val result = repository.getBusinessList(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 2,
        )

        // ASSERT
        result.test {
            assertEquals(
                expected = listOf(expectedBusiness),
                actual = awaitItem(),
            )
            awaitComplete()
        }
    }

//    @Test
//    fun `get business list error`() = runTest {
//        // ARRANGE
//        val httpClientEngine = MockEngine.Queue().apply {
//            enqueue {
//                respondError(status = HttpStatusCode.Unauthorized)
//            }
//        }
//        val repository = BusinessRestRepository(
//            api = getFakeBusinessApi(httpClientEngine),
//            dispatcher = UnconfinedTestDispatcher(),
//        )
//
//        // ACT
//        val result = repository.getBusinessList(
//            term = "term",
//            location = "location",
//            sortBy = "sortBy",
//            limit = 2,
//        )
//
//        // ASSERT
//        assertTrue(actual = result.isFailure)
//        assertTrue(actual = result.exceptionOrNull() is Exception)
//    }
//
//    @Test
//    fun `get business details with reviews success`() = runTest {
//        // ARRANGE
//        val httpClientEngine = MockEngine.Queue().apply {
//            enqueue {
//                respond(
//                    status = HttpStatusCode.OK,
//                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
//                    content = FileUtils.getStringFromPath(filePath = "responses/rest/getBusinessDetails.json"),
//                )
//            }
//            enqueue {
//                respond(
//                    status = HttpStatusCode.OK,
//                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
//                    content = FileUtils.getStringFromPath(filePath = "responses/rest/getBusinessReviews.json"),
//                )
//            }
//        }
//        val repository = BusinessRestRepository(
//            api = getFakeBusinessApi(httpClientEngine),
//            dispatcher = UnconfinedTestDispatcher(),
//        )
//
//        // ACT
//        val result = repository.getBusinessDetailsWithReviews(businessId = "businessId")
//
//        // ASSERT
//        assertEquals(
//            expected = Result.success(value = expectedBusinessDetailsWithReviews),
//            actual = result
//        )
//    }
//
//    @Test
//    fun `get business details with reviews throws an exception when calling getBusinessDetails`() = runTest {
//        // ARRANGE
//        val httpClientEngine = MockEngine.Queue().apply {
//            enqueue {
//                respondError(status = HttpStatusCode.Unauthorized)
//            }
//            enqueue {
//                respond(
//                    status = HttpStatusCode.OK,
//                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
//                    content = FileUtils.getStringFromPath(filePath = "responses/rest/getBusinessReviews.json"),
//                )
//            }
//        }
//        val repository = BusinessRestRepository(
//            api = getFakeBusinessApi(httpClientEngine),
//            dispatcher = UnconfinedTestDispatcher(),
//        )
//
//        // ACT
//        val result = repository.getBusinessDetailsWithReviews("businessId")
//
//        // ASSERT
//        assertTrue(actual = result.isFailure)
//        assertTrue(actual = result.exceptionOrNull() is Exception)
//    }
//
//    @Test
//    fun `get business details with reviews throws an exception when calling getBusinessReviews`() = runTest {
//        // ARRANGE
//        val httpClientEngine = MockEngine.Queue().apply {
//            enqueue {
//                respond(
//                    status = HttpStatusCode.OK,
//                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
//                    content = FileUtils.getStringFromPath(filePath = "responses/rest/getBusinessDetails.json"),
//                )
//            }
//            enqueue {
//                respondError(status = HttpStatusCode.Unauthorized)
//            }
//        }
//        val repository = BusinessRestRepository(
//            api = getFakeBusinessApi(httpClientEngine),
//            dispatcher = UnconfinedTestDispatcher(),
//        )
//
//        // ACT
//        val result = repository.getBusinessDetailsWithReviews(businessId = "businessId")
//
//        // ASSERT
//        assertTrue(actual = result.isFailure)
//        assertTrue(actual = result.exceptionOrNull() is Exception)
//    }

    private fun getFakeHttpClient(httpClientEngine: MockEngine): HttpClient {
        return HttpClient(engine = httpClientEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                )
            }
        }
    }
}
