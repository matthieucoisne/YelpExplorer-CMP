package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.core.utils.DateTimeFormatter
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeRestBusiness
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessRestMapperTest {

    private class FakeDateTimeFormatter(
        private val result: String,
    ) : DateTimeFormatter {
        override fun formatDate(dateTime: String) = result
        override fun formatTime(time: String) = "ignored"
    }

    @Test
    fun `map a list of BusinessEntity to a list of Business`() = runTest {
        // ARRANGE
        val expectedTimeCreated = "October 27, 2023" // TODO
        val expectedBusinessList = listOf(fakeDomainBusiness)
        val mapper = BusinessListRestMapperImpl(
            dateTimeFormatter = FakeDateTimeFormatter(expectedTimeCreated)
        )

        // ACT
        val actualBusinessList = mapper.map(listOf(fakeRestBusiness))

        // ASSERT
        assertEquals(
            expected = expectedBusinessList,
            actual = actualBusinessList,
        )
    }

    @Test
    fun `map a BusinessEntity to a Business`() = runTest {
        // ARRANGE
        val expectedTimeCreated = "October 27, 2023" // TODO
        val expectedBusiness = fakeDomainBusiness
        val mapper = BusinessDetailsRestMapperImpl(
            dateTimeFormatter = FakeDateTimeFormatter(expectedTimeCreated)
        )

        // ACT
        val actualBusiness = mapper.map(fakeRestBusiness)

        // ASSERT
        assertEquals(
            expected = expectedBusiness,
            actual = actualBusiness,
        )
    }
}
