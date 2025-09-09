package cmp.yelpexplorer.features.business.presentation.businesslist

import app.cash.turbine.test
import cmp.yelpexplorer.AppStateManager
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.getString
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.error_something_went_wrong
import yelpexplorer_cmp.composeapp.generated.resources.stars_small_4_half
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessListViewModelTest {

    // https://developer.android.com/kotlin/flow/test#continuous-collection
    private class FakeBusinessListUseCase(private val result: Result<List<Business>>) : BusinessListUseCase {
        override suspend fun execute(
            term: String,
            location: String,
            sortBy: String,
            limit: Int,
        ): Result<List<Business>> {
            delay(1) // Delay to ensure the initial state is captured by the test collector.
            return result
        }
    }

    private class FakeBusinessListMapper : BusinessListMapper {
        override suspend fun map(input: List<Business>): BusinessListUiModel {
            return BusinessListUiModel(
                businessList = emptyList(),
            )
        }
    }

    @Test
    fun `init success`() = runTest {
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
                result = Result.success(value = listOf(fakeBusiness))
            ),
            businessListMapper = FakeBusinessListMapper(),
            appStateManager = AppStateManager(),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading(),
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
    fun `init error`() = runTest {
        // ARRANGE
        val viewModel = BusinessListViewModel(
            businessListUseCase = FakeBusinessListUseCase(
                result = Result.failure(Exception())
            ),
            businessListMapper = FakeBusinessListMapper(),
            appStateManager = AppStateManager(),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading(),
                actual = awaitItem()
            )
            assertEquals(
                expected = BusinessListViewState.ShowError(
                    error = getString(Res.string.error_something_went_wrong)
                ),
                actual = awaitItem()
            )
        }
    }
}
