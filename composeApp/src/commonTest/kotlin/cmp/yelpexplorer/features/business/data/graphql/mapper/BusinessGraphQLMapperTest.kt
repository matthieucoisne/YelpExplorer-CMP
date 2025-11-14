package cmp.yelpexplorer.features.business.data.graphql.mapper

import cmp.yelpexplorer.BusinessDetailsQuery
import cmp.yelpexplorer.BusinessListQuery
import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeDomainBusinessDetailsWithReviews
import cmp.yelpexplorer.utils.fakeGraphQLBusinessDetails
import cmp.yelpexplorer.utils.fakeGraphQLBusinessSummary
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessGraphQLMapperTest {

    private class FakeDateTimeFormatter(
        private val result: String = "",
    ) : DateTimeFormatter {
        override fun formatDate(dateTime: String) = result
        override fun formatTime(time: String) = "10:00"
    }

    @Test
    fun `map a list of BusinessListQuery Business to a list of domain Business`() = runTest {
        // ARRANGE
        val businessList = listOf(
            BusinessListQuery.Business(
                __typename = "Business",
                businessSummary = fakeGraphQLBusinessSummary
            )
        )
        val mapper = BusinessListGraphQLMapperImpl(
            dateTimeFormatter = FakeDateTimeFormatter()
        )

        // ACT
        val result = mapper.map(businessList)

        // ASSERT
        assertEquals(
            expected = listOf(fakeDomainBusiness),
            actual = result,
        )
    }

    @Test
    fun `map a BusinessDetailsQuery Business to a domain Business`() = runTest {
        // ARRANGE
        val business = BusinessDetailsQuery.Business(
            __typename = "Business",
            businessSummary = fakeGraphQLBusinessSummary,
            businessDetails = fakeGraphQLBusinessDetails,
        )

        val expectedTimeCreated = "October 27, 2023" // TODO

        val mapper = BusinessDetailsGraphQLMapperImpl(
            dateTimeFormatter = FakeDateTimeFormatter(expectedTimeCreated)
        )

        // ACT
        val result = mapper.map(business)

        // ASSERT
        assertEquals(
            expected = fakeDomainBusinessDetailsWithReviews,
            actual = result,
        )
    }
}
