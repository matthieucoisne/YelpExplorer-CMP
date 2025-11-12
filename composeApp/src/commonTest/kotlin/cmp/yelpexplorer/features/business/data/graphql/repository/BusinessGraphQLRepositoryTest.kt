package cmp.yelpexplorer.features.business.data.graphql.repository

import app.cash.turbine.test
import cmp.yelpexplorer.core.utils.DateTimeFormater
import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSourceImpl
import cmp.yelpexplorer.features.business.data.graphql.mapper.BusinessGraphQLMapper
import com.apollographql.apollo.ApolloClient
import com.apollographql.mockserver.MockServer
import com.apollographql.mockserver.MockResponse
import cmp.yelpexplorer.utils.FileUtils
import com.apollographql.apollo.exception.NoDataException
import com.apollographql.mockserver.enqueueError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.reflect.typeOf
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessGraphQLRepositoryTest : BusinessRepositoryTest() {

    private lateinit var mockServer: MockServer
    private lateinit var repository: BusinessGraphQLRepository

    private fun getApolloClient(mockServer: MockServer): ApolloClient {
        return runBlocking {
            ApolloClient.Builder()
                .serverUrl(serverUrl = mockServer.url())
                .build()
        }
    }

    @BeforeTest
    fun before() {
        mockServer = MockServer()
        repository = BusinessGraphQLRepository(
            businessGraphQLDataSource = BusinessGraphQLDataSourceImpl(getApolloClient(mockServer)),
            businessGraphQLMapper = BusinessGraphQLMapper(DateTimeFormater()),
            ioDispatcher = UnconfinedTestDispatcher(),
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
        result.test {
            assertEquals(
                expected = listOf(expectedBusiness),
                actual = awaitItem(),
            )
            awaitComplete()
        }
    }

    @Test
    fun `get business list error`() = runTest {
        // ARRANGE
        mockServer.enqueueError(500)

        // ACT
        val result = repository.getBusinessList(
            term = "term",
            location = "location",
            sortBy = "sortBy",
            limit = 2
        )

        // ASSERT
        result.test {
            assertTrue { awaitError() is NoDataException }
        }
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
        result.test {
            assertEquals(
                expected = expectedBusinessDetailsWithReviews,
                actual = awaitItem(),
            )
            awaitComplete()
        }
    }

    @Test
    fun `get business details with reviews error`() = runTest {
        // ARRANGE
        mockServer.enqueueError(500)

        // ACT
        val result = repository.getBusinessDetailsWithReviews(businessId = "businessId")

        // ASSERT
        result.test {
            assertTrue { awaitError() is NoDataException }
        }
    }
}
