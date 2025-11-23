package cmp.yelpexplorer.features.business.presentation.businesslist

import app.cash.turbine.test
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCase
import cmp.yelpexplorer.utils.fakeBusinessListUiModel
import cmp.yelpexplorer.utils.fakeDomainBusiness
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeBusinessListUseCase(
        private val error: Exception? = null,
    ) : BusinessListUseCase {
        override fun execute(
            term: String,
            location: String,
            sortBy: String,
            limit: Int,
        ): Flow<List<Business>> = flow {
            if (error != null) throw error
            emit(listOf(fakeDomainBusiness))
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
    fun `viewState is ShowLoading then ShowBusinessList`() = runTest(testDispatcher) {
        // ARRANGE
        val viewModel = BusinessListViewModel(
            businessListUseCase = FakeBusinessListUseCase(),
            businessListMapper = FakeBusinessListMapper(fakeBusinessListUiModel),
            mainDispatcher = testDispatcher,
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading,
                actual = awaitItem(),
            )
            advanceUntilIdle()
            assertEquals(
                expected = BusinessListViewState.ShowBusinessList(
                    businessList = fakeBusinessListUiModel,
                ),
                actual = awaitItem(),
            )
            // awaitComplete() - NEVER COMPLETES
        }
    }

    @Test
    fun `viewState is ShowLoading then ShowError`() = runTest(testDispatcher) {
        // ARRANGE
        val viewModel = BusinessListViewModel(
            businessListUseCase = FakeBusinessListUseCase(
                error = Exception(),
            ),
            businessListMapper = FakeBusinessListMapper(),
            mainDispatcher = testDispatcher,
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessListViewState.ShowLoading,
                actual = awaitItem(),
            )
            advanceUntilIdle()
            assertEquals(
                expected = BusinessListViewState.ShowError,
                actual = awaitItem(),
            )
            // awaitComplete() - NEVER COMPLETES
        }
    }
}
