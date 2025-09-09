package cmp.yelpexplorer.features.business.presentation.businessdetails

sealed interface BusinessDetailsViewState {
    data object ShowLoading : BusinessDetailsViewState
    data class ShowBusinessDetails(val businessDetails: BusinessDetailsUiModel) : BusinessDetailsViewState
    data class ShowError(val error: String) : BusinessDetailsViewState
}
