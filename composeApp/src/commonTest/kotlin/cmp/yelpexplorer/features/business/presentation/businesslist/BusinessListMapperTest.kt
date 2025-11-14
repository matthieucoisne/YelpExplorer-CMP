package cmp.yelpexplorer.features.business.presentation.businesslist

import cmp.yelpexplorer.utils.fakeBusinessListUiModel
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeResourceProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessListMapperTest {

    @Test
    fun `map a list of Business to a BusinessListUiModel`() = runTest {
        // ARRANGE
        val mapper = BusinessListMapperImpl(fakeResourceProvider)

        // ACT
        val businessListUiModel = mapper.map(listOf(fakeDomainBusiness))

        // ASSERT
        assertEquals(
            expected = fakeBusinessListUiModel,
            actual = businessListUiModel,
        )
    }
}
