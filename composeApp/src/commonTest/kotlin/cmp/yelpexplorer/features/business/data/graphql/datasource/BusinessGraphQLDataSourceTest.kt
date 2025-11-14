package cmp.yelpexplorer.features.business.data.graphql.datasource

import cmp.yelpexplorer.features.business.data.graphql.datasource.remote.BusinessGraphQLDataSourceImpl
import cmp.yelpexplorer.utils.FileUtils
import cmp.yelpexplorer.utils.fakeGraphQLBusinessDetails
import cmp.yelpexplorer.utils.fakeGraphQLBusinessSummary
import com.apollographql.apollo.ApolloClient
import com.apollographql.mockserver.MockResponse
import com.apollographql.mockserver.MockServer
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BusinessGraphQLDataSourceTest {

    private suspend fun getApolloClient(
        mockServer: MockServer
    ) = ApolloClient.Builder()
        .serverUrl(serverUrl = mockServer.url())
        .build()

    private lateinit var mockServer: MockServer

    @BeforeTest
    fun before() {
        mockServer = MockServer()
    }

    @AfterTest
    fun after() {
        mockServer.close()
    }

    @Test
    fun `get business list success`() = runTest {
        // ARRANGE
        val jsonBusiness = FileUtils.getStringFromPath(
            filePath = "responses/graphql/businessList.json",
        )
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = jsonBusiness)
                .build()
        )
        val dataSource = BusinessGraphQLDataSourceImpl(
            apolloClient = getApolloClient(mockServer),
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
            expected = listOf(fakeGraphQLBusinessSummary),
            actual = result,
        )
    }

    @Test
    fun `get business list error`() = runTest {
        // ARRANGE
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = "Internal server error")
                .statusCode(500)
                .build()
        )
        val dataSource = BusinessGraphQLDataSourceImpl(
            apolloClient = getApolloClient(mockServer),
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
        val jsonBusinessDetails = FileUtils.getStringFromPath(
            filePath = "responses/graphql/businessDetailsWithReviews.json",
        )
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = jsonBusinessDetails)
                .build()
        )
        val dataSource = BusinessGraphQLDataSourceImpl(
            apolloClient = getApolloClient(mockServer),
        )

        // ACT
        val result = dataSource.getBusinessDetails(
            businessId = "businessId",
        )

        // ASSERT
        assertEquals(
            expected = fakeGraphQLBusinessDetails,
            actual = result,
        )
    }

    @Test
    fun `get business details error`() = runTest {
        // ARRANGE
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = "Internal server error")
                .statusCode(500)
                .build()
        )
        val dataSource = BusinessGraphQLDataSourceImpl(
            apolloClient = getApolloClient(mockServer),
        )

        // ACT & ASSERT
        assertFails {
            dataSource.getBusinessDetails(
                businessId = "businessId",
            )
        }
    }
}
