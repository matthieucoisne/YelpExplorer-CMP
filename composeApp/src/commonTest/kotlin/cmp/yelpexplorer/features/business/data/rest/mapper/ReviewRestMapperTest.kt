package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.utils.fakeDomainReview
import cmp.yelpexplorer.utils.fakeRestReview
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewRestMapperTest {

    private class FakeDateTimeFormatter(
        private val result: String,
    ) : DateTimeFormatter {
        override fun formatDate(dateTime: String) = result
        override fun formatTime(time: String) = "ignored"
    }

    @Test
    fun `map a list of ReviewEntities to a list of Reviews with single line break text and custom date format`() = runTest {
        // ARRANGE
        val reviewEntities = listOf(fakeRestReview)
        val expectedTimeCreated = "October 27, 2023"
        val expectedReviews = listOf(fakeDomainReview)
        val mapper = ReviewRestMapperImpl(
            dateTimeFormatter = FakeDateTimeFormatter(expectedTimeCreated)
        )

        // ACT
        val actualReviews = mapper.map(reviewEntities)

        // ASSERT
        assertEquals(
            expected = expectedReviews,
            actual = actualReviews,
        )
    }
}
