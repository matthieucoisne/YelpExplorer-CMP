package cmp.yelpexplorer.features.business.presentation.businesslist

import app.cash.turbine.test
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.GetBusinessListUseCase
import cmp.yelpexplorer.utils.fakeBusinessListUiModel
import cmp.yelpexplorer.utils.fakeDomainBusiness
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private class GetFakeBusinessListUseCase(
        private val error: Exception? = null,
    ) : GetBusinessListUseCase {
        override fun invoke(
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
            getBusinessListUseCase = GetFakeBusinessListUseCase(),
            businessListMapper = FakeBusinessListMapper(fakeBusinessListUiModel),
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
            getBusinessListUseCase = GetFakeBusinessListUseCase(
                error = Exception(),
            ),
            businessListMapper = FakeBusinessListMapper(),
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
