package cmp.yelpexplorer.features.business.presentation.businessdetails

import cmp.yelpexplorer.core.utils.ResourceProvider
import cmp.yelpexplorer.utils.fakeBusinessDetailsUiModel
import cmp.yelpexplorer.utils.fakeDomainBusiness
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.StringResource
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessDetailsMapperTest {

    private val mockResourceProvider = object : ResourceProvider {
        override suspend fun getResourceString(stringResource: StringResource) = ""
        override fun getStarRating(rating: Double) = Res.drawable.stars_small_4 // TODO
        override suspend fun getDayName(day: Int) = ""
    }

    private val mapper = BusinessDetailsMapperImpl(mockResourceProvider)

    @Test
    fun `map a Business to a BusinessDetailUiModel`() = runTest {
        // ACT
        val businessDetailsUiModel = mapper.map(fakeDomainBusiness)

        // ASSERT
        assertEquals(
            expected = fakeBusinessDetailsUiModel,
            actual = businessDetailsUiModel,
        )
    }
}
