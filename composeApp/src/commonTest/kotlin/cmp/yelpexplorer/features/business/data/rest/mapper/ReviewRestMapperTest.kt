package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.utils.fakeDateTimeFormatter
import cmp.yelpexplorer.utils.fakeDomainReview
import cmp.yelpexplorer.utils.fakeRestReview
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewRestMapperTest {

    @Test
    fun `map a list of ReviewEntities to a list of Reviews with single line break text and custom date format`() = runTest {
        // ARRANGE
        val mapper = ReviewRestMapperImpl(
            dateTimeFormatter = fakeDateTimeFormatter
        )

        // ACT
        val result = mapper.map(listOf(fakeRestReview))

        // ASSERT
        assertEquals(
            expected = listOf(fakeDomainReview),
            actual = result,
        )
    }
}
