package cmp.yelpexplorer.features.business.presentation.businessdetails

import cmp.yelpexplorer.utils.fakeBusinessDetailsUiModel
import cmp.yelpexplorer.utils.fakeDomainBusiness
import cmp.yelpexplorer.utils.fakeResourceProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessDetailsMapperTest {

    @Test
    fun `map a Business to a BusinessDetailUiModel`() = runTest {
        // ARRANGE
        val mapper = BusinessDetailsMapperImpl(fakeResourceProvider)

        // ACT
        val businessDetailsUiModel = mapper.map(fakeDomainBusiness)

        // ASSERT
        assertEquals(
            expected = fakeBusinessDetailsUiModel,
            actual = businessDetailsUiModel,
        )
    }
}
