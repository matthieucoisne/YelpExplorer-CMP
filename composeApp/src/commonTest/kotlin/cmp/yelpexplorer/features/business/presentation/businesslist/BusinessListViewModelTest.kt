package cmp.yelpexplorer.features.business.presentation.businesslist

import app.cash.turbine.test
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4_half
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessListViewModelTest {

    // https://developer.android.com/kotlin/flow/test#continuous-collection
    private class FakeBusinessListUseCase(
        private val result: List<Business> = emptyList(),
        private val error: Exception? = null,
    ) : BusinessListUseCase {
        override fun execute(
            term: String,
            location: String,
            sortBy: String,
            limit: Int,
        ): Flow<List<Business>> = flow {
            if (error != null) {
                throw error
            } else {
                emit(result)
            }
        }
    }

    private class FakeBusinessListMapper(
        private val result: List<BusinessUiModel> = emptyList(),
    ) : BusinessListMapper {
        override suspend fun map(input: List<Business>): BusinessListUiModel {
            return BusinessListUiModel(
                businessList = result,
            )
        }
    }

    @Test
    fun `viewState is ShowLoading then ShowBusinessList`() = runTest {
        // ARRANGE
        val fakeBusiness = Business(
            id = "id",
            name = "name",
            address = "address",
            photoUrl = "http://",
            price = "$$",
            categories = listOf("category#1"),
            reviewCount = 1337,
            rating = 4.5,
            hours = null,
            reviews = null,
        )
        val expectedBusinessUiModel = BusinessUiModel(
            id = "id",
            name = "name",
            address = "address",
            photoUrl = "http://",
            priceAndCategories = "$$ - category#1",
            reviewCount = 1337,
            rating = Res.drawable.stars_small_4_half,
        )
        val viewModel = BusinessListViewModel(
            businessListUseCase = FakeBusinessListUseCase(
                result = listOf(fakeBusiness)
            ),
            businessListMapper = FakeBusinessListMapper(
                result = listOf(expectedBusinessUiModel)
            ),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading,
                actual = awaitItem()
            )
            assertEquals(
                expected = BusinessListViewState.ShowBusinessList(
                    businessList = BusinessListUiModel(businessList = listOf(expectedBusinessUiModel))
                ),
                actual = awaitItem()
            )
        }
    }

    @Test
    fun `viewState is ShowLoading then ShowError`() = runTest {
        // ARRANGE
        val viewModel = BusinessListViewModel(
            businessListUseCase = FakeBusinessListUseCase(
                error = Exception("error"),
            ),
            businessListMapper = FakeBusinessListMapper(),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading,
                actual = awaitItem()
            )
            assertEquals(
                expected = BusinessListViewState.ShowError("error"),
                actual = awaitItem()
            )
        }
    }
}
