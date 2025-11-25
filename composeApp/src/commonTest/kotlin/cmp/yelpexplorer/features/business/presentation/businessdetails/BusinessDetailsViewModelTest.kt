package cmp.yelpexplorer.features.business.presentation.businessdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.GetBusinessDetailsUseCase
import cmp.yelpexplorer.utils.fakeBusinessDetailsUiModel
import cmp.yelpexplorer.utils.fakeDomainBusiness
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BusinessDetailsViewModelTest {

    private class GetFakeBusinessDetailsUseCase(
        private val error: Exception? = null,
    ) : GetBusinessDetailsUseCase {
        override fun invoke(businessId: String): Flow<Business> = flow {
            if (error != null) throw error
            emit(fakeDomainBusiness)
        }
    }

    private class FakeBusinessDetailsMapper(
        private val result: BusinessDetailsUiModel? = null,
    ) : BusinessDetailsMapper {
        override suspend fun map(input: Business): BusinessDetailsUiModel {
            return result!!
        }
    }

    @Test
    fun `viewState is ShowLoading then ShowBusinessDetails`() = runTest {
        // ARRANGE
        val viewModel = BusinessDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf("businessId" to "businessId"),
            ),
            getBusinessDetailsUseCase = GetFakeBusinessDetailsUseCase(),
            businessDetailsMapper = FakeBusinessDetailsMapper(
                fakeBusinessDetailsUiModel,
            ),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessDetailsViewState.ShowLoading,
                actual = awaitItem(),
            )
            assertEquals(
                expected = BusinessDetailsViewState.ShowBusinessDetails(
                    businessDetails = fakeBusinessDetailsUiModel,
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
        val viewModel = BusinessDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf("businessId" to "businessId"),
            ),
            getBusinessDetailsUseCase = GetFakeBusinessDetailsUseCase(
                error = Exception(expectedResult),
            ),
            businessDetailsMapper = FakeBusinessDetailsMapper(),
        )

        // ACT & ASSERT
        viewModel.viewState.test {
            assertEquals(
                expected = BusinessDetailsViewState.ShowLoading,
                actual = awaitItem(),
            )
            assertEquals(
                expected = BusinessDetailsViewState.ShowError,
                actual = awaitItem(),
            )
            // awaitComplete() - NEVER COMPLETES
        }
    }
}
