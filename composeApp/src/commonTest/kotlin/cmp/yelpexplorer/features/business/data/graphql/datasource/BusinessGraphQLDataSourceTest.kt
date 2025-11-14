package cmp.yelpexplorer.features.business.data.graphql.datasource

import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery
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

class BusinessGraphQLDataSourceTest {

    private lateinit var mockServer: MockServer

    private suspend fun getApolloClient(
        mockServer: MockServer
    ) = ApolloClient.Builder()
        .serverUrl(serverUrl = mockServer.url())
        .build()

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
            filePath = "responses/graphql/businessList.json"
        )
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = jsonBusiness)
                .build()
        )
        val expectedBusiness = BusinessListQuery.Business(
            __typename = "Business",
            businessSummary = fakeGraphQLBusinessSummary,
        )
        val dataSource = BusinessGraphQLDataSourceImpl(
            apolloClient = getApolloClient(mockServer)
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
            expected = listOf(expectedBusiness),
            actual = result,
        )
    }

    @Test
    fun `get business details success`() = runTest {
        // ARRANGE
        val jsonBusinessDetails = FileUtils.getStringFromPath(
            filePath = "responses/graphql/businessDetailsWithReviews.json"
        )
        mockServer.enqueue(
            MockResponse.Builder()
                .body(body = jsonBusinessDetails)
                .build()
        )
        val expectedBusinessDetails = BusinessDetailsQuery.Business( // TODO extract
            __typename = "Business",
            businessSummary = fakeGraphQLBusinessSummary,
            businessDetails = fakeGraphQLBusinessDetails,
        )
        val dataSource = BusinessGraphQLDataSourceImpl(
            apolloClient = getApolloClient(mockServer)
        )

        // ACT
        val result = dataSource.getBusinessDetails(
            businessId = "businessId"
        )

        // ASSERT
        assertEquals(
            expected = expectedBusinessDetails,
            actual = result,
        )
    }

    // TODO fails
}
