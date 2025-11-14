package cmp.yelpexplorer.features.business.presentation.businesslist

import app.cash.turbine.test
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCase
import cmp.yelpexplorer.utils.fakeBusinessListUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessListViewModelTest {

    private class FakeBusinessListUseCase(
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
                emit(emptyList())
            }
        }
    }

    private class FakeBusinessListMapper(
        private val result: BusinessListUiModel? = null,
    ) : BusinessListMapper {
        override suspend fun map(input: List<Business>): BusinessListUiModel {
            return result!!
        }
    }

    @Test
    fun `viewState is ShowLoading then ShowBusinessList`() = runTest {
        // ARRANGE
        val viewModel = BusinessListViewModel(
            businessListUseCase = FakeBusinessListUseCase(),
            businessListMapper = FakeBusinessListMapper(fakeBusinessListUiModel),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading,
                actual = awaitItem(),
            )
            assertEquals(
                expected = BusinessListViewState.ShowBusinessList(
                    businessList = fakeBusinessListUiModel
                ),
                actual = awaitItem(),
            )
            // awaitComplete() - NEVER COMPLETES
        }
    }

    @Test
    fun `viewState is ShowLoading then ShowError`() = runTest {
        // ARRANGE
        val expectedResult = "error"
        val viewModel = BusinessListViewModel(
            businessListUseCase = FakeBusinessListUseCase(
                error = Exception(expectedResult),
            ),
            businessListMapper = FakeBusinessListMapper(),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading,
                actual = awaitItem(),
            )
            assertEquals(
                expected = BusinessListViewState.ShowError(error = expectedResult),
                actual = awaitItem(),
            )
            // awaitComplete() - NEVER COMPLETES
        }
    }
}
