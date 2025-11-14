package cmp.yelpexplorer.features.business.data.rest.datasource

import cmp.yelpexplorer.features.business.data.rest.datasource.remote.BusinessRestDataSourceImpl
import cmp.yelpexplorer.features.business.data.rest.model.BusinessEntity
import cmp.yelpexplorer.features.business.data.rest.model.BusinessListResponse
import cmp.yelpexplorer.features.business.data.rest.model.ReviewListResponse
import cmp.yelpexplorer.utils.FileUtils
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BusinessRestDataSourceTest {

    private fun getFakeHttpClient(mockEngine: MockEngine): HttpClient {
        return HttpClient(engine = mockEngine) {
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

    @Test
    fun `get business list success`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine.Queue().apply {
            enqueue {
                respond(
                    status = HttpStatusCode.OK,
                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                    content = FileUtils.getStringFromPath(
                        filePath = "responses/rest/getBusinessList.json",
                    ),
                )
            }
        }
        val expectedBusinessListResponse = FileUtils.getDataFromPath<BusinessListResponse>(
            filePath = "responses/rest/getBusinessList.json",
        )
        val dataSource = BusinessRestDataSourceImpl(
            httpClient = getFakeHttpClient(mockEngine = mockEngine),
        )

        // ACT
        val result = dataSource.getBusinessList(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 20,
        )

        // ASSERT
        assertEquals(
            expected = expectedBusinessListResponse,
            actual = result,
        )
    }

    @Test
    fun `get business list error`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine.Queue().apply {
            enqueue {
                respond(
                    status = HttpStatusCode.InternalServerError,
                    content = "",
                )
            }
        }
        val dataSource = BusinessRestDataSourceImpl(
            httpClient = getFakeHttpClient(mockEngine = mockEngine),
        )

        // ACT & ASSERT
        assertFails {
            dataSource.getBusinessList(
                term = "term",
                location = "location",
                sortBy = "sortBy",
                limit = 20,
            )
        }
    }

    @Test
    fun `get business details success`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine.Queue().apply {
            enqueue {
                respond(
                    status = HttpStatusCode.OK,
                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                    content = FileUtils.getStringFromPath(
                        filePath = "responses/rest/getBusinessDetails.json",
                    ),
                )
            }
        }
        val expectedBusinessEntity = FileUtils.getDataFromPath<BusinessEntity>(
            filePath = "responses/rest/getBusinessDetails.json",
        )
        val dataSource = BusinessRestDataSourceImpl(
            httpClient = getFakeHttpClient(mockEngine = mockEngine),
        )

        // ACT
        val result = dataSource.getBusinessDetails(
            businessId = "businessId",
        )

        // ASSERT
        assertEquals(
            expected = expectedBusinessEntity,
            actual = result,
        )
    }

    @Test
    fun `get business details error`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine.Queue().apply {
            enqueue {
                respond(
                    status = HttpStatusCode.InternalServerError,
                    content = "",
                )
            }
        }
        val dataSource = BusinessRestDataSourceImpl(
            httpClient = getFakeHttpClient(mockEngine = mockEngine),
        )

        // ACT & ASSERT
        assertFails {
            dataSource.getBusinessDetails(
                businessId = "businessId",
            )
        }
    }

    @Test
    fun `get business reviews success`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine.Queue().apply {
            enqueue {
                respond(
                    status = HttpStatusCode.OK,
                    headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
                    content = FileUtils.getStringFromPath(
                        filePath = "responses/rest/getBusinessReviews.json",
                    ),
                )
            }
        }
        val expectedReviewListResponse = FileUtils.getDataFromPath<ReviewListResponse>(
            filePath = "responses/rest/getBusinessReviews.json",
        )
        val dataSource = BusinessRestDataSourceImpl(
            httpClient = getFakeHttpClient(mockEngine = mockEngine),
        )

        // ACT
        val result = dataSource.getBusinessReviews(
            businessId = "businessId",
        )

        // ASSERT
        assertEquals(
            expected = expectedReviewListResponse,
            actual = result,
        )
    }

    @Test
    fun `get business reviews error`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine.Queue().apply {
            enqueue {
                respond(
                    status = HttpStatusCode.InternalServerError,
                    content = "",
                )
            }
        }
        val dataSource = BusinessRestDataSourceImpl(
            httpClient = getFakeHttpClient(mockEngine = mockEngine),
        )

        // ACT & ASSERT
        assertFails {
            dataSource.getBusinessReviews(
                businessId = "businessId",
            )
        }
    }
}
