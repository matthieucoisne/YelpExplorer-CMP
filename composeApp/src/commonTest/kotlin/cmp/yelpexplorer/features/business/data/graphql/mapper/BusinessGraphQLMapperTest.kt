package cmp.yelpexplorer.features.business.data.graphql.mapper

import cmp.yelpexplorer.utils.fakeDateTimeFormatter
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeDomainBusinessDetailsWithReviews
import cmp.yelpexplorer.utils.fakeGraphQLBusinessDetails
import cmp.yelpexplorer.utils.fakeGraphQLBusinessSummary
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessGraphQLMapperTest {

    @Test
    fun `map a list of BusinessListQuery Business to a list of domain Business`() = runTest {
        // ARRANGE
        val mapper = BusinessListGraphQLMapperImpl(
            dateTimeFormatter = fakeDateTimeFormatter,
        )

        // ACT
        val result = mapper.map(
            listOf(fakeGraphQLBusinessSummary),
        )

        // ASSERT
        assertEquals(
            expected = listOf(fakeDomainBusiness),
            actual = result,
        )
    }

    @Test
    fun `map a BusinessDetailsQuery Business to a domain Business`() = runTest {
        // ARRANGE
        val mapper = BusinessDetailsGraphQLMapperImpl(
            dateTimeFormatter = fakeDateTimeFormatter,
        )

        // ACT
        val result = mapper.map(
            fakeGraphQLBusinessDetails,
        )

        // ASSERT
        assertEquals(
            expected = fakeDomainBusinessDetailsWithReviews,
            actual = result,
        )
    }
}
