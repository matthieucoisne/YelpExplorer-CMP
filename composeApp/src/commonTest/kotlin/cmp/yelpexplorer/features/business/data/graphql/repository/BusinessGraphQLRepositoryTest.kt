package cmp.yelpexplorer.features.business.data.graphql.repository

import cmp.yelpexplorer.core.utils.DateTimeFormater
import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSourceImpl
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessGraphQLMapper
import com.apollographql.apollo.ApolloClient
import com.apollographql.mockserver.MockServer
import com.apollographql.mockserver.MockResponse
import cmp.yelpexplorer.utils.FileUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessGraphQLRepositoryTest : BusinessRepositoryTest() {

    private lateinit var mockServer: MockServer
    private lateinit var repository: BusinessGraphQLRepository

    @BeforeTest
    fun before() {
        mockServer = MockServer()
        repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = BusinessGraphQLDataSourceImpl(getApolloClient(mockServer)),
            businessGraphQLMapper = BusinessGraphQLMapper(DateTimeFormater()),
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @AfterTest
    fun after() {
        mockServer.close()
    }

    @Test
    fun `get business list success`() = runTest {
        // ARRANGE
        val jsonBusiness = FileUtils.getStringFromPath(filePath = "responses/graphql/businessList.json")
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = jsonBusiness)
                .build()
        )

        // ACT
        val result = repository.getBusinessList(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 2
        )

        // ASSERT
        assertEquals(actual = result, expected = Result.success(value = listOf(expectedBusiness)))
    }

    @Test
    fun `get business list error`() = runTest {
        // ARRANGE
        mockServer.enqueue(
            MockResponse.Builder()
                .statusCode(statusCode = 500)
                .body(body = "Internal server error")
                .build()
        )

        // ACT
        val result = repository.getBusinessList(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 2
        )

        // ASSERT
        assertTrue(actual = result.isFailure)
        assertTrue(actual = result.exceptionOrNull() is Exception)
    }

    @Test
    fun `get business details with reviews success`() = runTest {
        // ARRANGE
        val jsonBusinessDetailsWithReviews = FileUtils.getStringFromPath(filePath = "responses/graphql/businessDetailsWithReviews.json")
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = jsonBusinessDetailsWithReviews)
                .build()
        )

        // ACT
        val result = repository.getBusinessDetailsWithReviews(businessId = "businessId")

        // ASSERT
        assertEquals(actual = result, expected = Result.success(value = expectedBusinessDetailsWithReviews))
    }

    @Test
    fun `get business details error`() = runTest {
        // ARRANGE
        mockServer.enqueue(
            MockResponse.Builder()
                .statusCode(statusCode = 500)
                .body(body = "Internal server error")
                .build()
        )

        // ACT
        val result = repository.getBusinessDetailsWithReviews(businessId = "businessId")

        // ASSERT
        assertTrue(actual = result.isFailure)
        assertTrue(actual = result.exceptionOrNull() is Exception)
    }

    private fun getApolloClient(mockServer: MockServer): ApolloClient {
        return runBlocking {
            ApolloClient.Builder()
                .serverUrl(serverUrl = mockServer.url())
                .build()
        }
    }
}
