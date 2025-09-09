//package cmp.yelpexplorer.features.business.presentation.businessdetails
//
//import androidx.lifecycle.SavedStateHandle
//import app.cash.turbine.test
//import cmp.yelpexplorer.features.business.domain.model.Business
//import cmp.yelpexplorer.features.business.domain.usecase.BusinessDetailsUseCase
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.test.runTest
//import yelpexplorer_cmp.composeapp.generated.resources.Res
//import yelpexplorer_cmp.composeapp.generated.resources.error_something_went_wrong
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class BusinessDetailsViewModelTest {
//
//    // https://developer.android.com/kotlin/flow/test#continuous-collection
//    private inner class FakeBusinessDetailsUseCase(private val result: Result<Business>) : BusinessDetailsUseCase {
//        override suspend fun execute(businessId: String): Result<Business> {
//            delay(1) // Delay to ensure the initial state is captured by the test collector.
//            return result
//        }
//    }
//
//    @Test
//    fun `init success`() = runTest {
//        // ARRANGE
//        val fakeBusiness = Business(
//            id = "id",
//            name = "name",
//            address = "address",
//            photoUrl = "http://",
//            price = "$$",
//            categories = listOf("category#1"),
//            reviewCount = 1337,
//            rating = 4.5,
//            hours = null,
//            reviews = null,
//        )
//        val expectedBusinessDetailsUiModel = BusinessDetailsUiModel(
//            id = "id",
//            name = "name",
//            photoUrl = "http://",
//            rating = 4.5,
//            reviewCount = 1337,
//            address = "address",
//            price = "$$",
//            categories = "category#1",
//            openingHours = emptyMap(),
//            reviews = emptyList(),
//        )
//        val viewModel = BusinessDetailsViewModel(
//            getBusinessDetailsUseCase = FakeBusinessDetailsUseCase(Result.success(value = fakeBusiness)),
//            savedStateHandle = SavedStateHandle(initialState = mapOf("businessId" to "businessId")),
//        )
//
//        // ACT & ASSERT
//        viewModel.uiState.test {
//            assertEquals(
//                expected = BusinessDetailsViewModel.ViewState.ShowLoading,
//                actual = awaitItem()
//            )
//            assertEquals(
//                expected = BusinessDetailsViewModel.ViewState.ShowBusinessDetails(
//                    businessDetails = expectedBusinessDetailsUiModel
//                ),
//                actual = awaitItem()
//            )
//        }
//    }
//
//    @Test
//    fun `init error`() = runTest {
//        // ARRANGE
//        val viewModel = BusinessDetailsViewModel(
//            getBusinessDetailsUseCase = FakeBusinessDetailsUseCase(Result.failure(Exception())),
//            savedStateHandle = SavedStateHandle(initialState = mapOf("businessId" to "businessId")),
//        )
//
//        // ACT & ASSERT
//        viewModel.uiState.test {
//            assertEquals(
//                expected = BusinessDetailsViewModel.ViewState.ShowLoading,
//                actual = awaitItem()
//            )
//            assertEquals(
//                expected = BusinessDetailsViewModel.ViewState.ShowError(
//                    error = Res.string.error_something_went_wrong
//                ),
//                actual = awaitItem()
//            )
//        }
//    }
//}
