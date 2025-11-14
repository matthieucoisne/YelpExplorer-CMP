package cmp.yelpexplorer.features.business.presentation.businesslist

import cmp.yelpexplorer.core.utils.ResourceProvider
import cmp.yelpexplorer.utils.fakeBusinessListUiModel
import cmp.yelpexplorer.utils.fakeDomainBusiness
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.StringResource
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessListMapperTest {

    private val mockResourceProvider = object : ResourceProvider {
        override suspend fun getResourceString(stringResource: StringResource) = ""
        override fun getStarRating(rating: Double) = Res.drawable.stars_small_4 // TODO
        override suspend fun getDayName(day: Int) = ""
    }

    private val mapper = BusinessListMapperImpl(mockResourceProvider)

    @Test
    fun `map a list of Business to a BusinessListUiModel`() = runTest {
        // ARRANGE
        val fakeBusinessList = listOf(fakeDomainBusiness)

        // ACT
        val businessListUiModel = mapper.map(fakeBusinessList)

        // ASSERT
        assertEquals(
            expected = fakeBusinessListUiModel,
            actual = businessListUiModel,
        )
    }
}
