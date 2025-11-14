package cmp.yelpexplorer.features.business.data.rest.mapper

import cmp.yelpexplorer.utils.fakeDateTimeFormatter
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeRestBusiness
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessRestMapperTest {

    @Test
    fun `map a list of BusinessEntity to a list of Business`() = runTest {
        // ARRANGE
        val mapper = BusinessListRestMapperImpl(
            dateTimeFormatter = fakeDateTimeFormatter,
        )

        // ACT
        val result = mapper.map(listOf(fakeRestBusiness))

        // ASSERT
        assertEquals(
            expected = listOf(fakeDomainBusiness),
            actual = result,
        )
    }

    @Test
    fun `map a BusinessEntity to a Business`() = runTest {
        // ARRANGE
        val mapper = BusinessDetailsRestMapperImpl(
            dateTimeFormatter = fakeDateTimeFormatter,
        )

        // ACT
        val result = mapper.map(fakeRestBusiness)

        // ASSERT
        assertEquals(
            expected = fakeDomainBusiness,
            actual = result,
        )
    }
}
