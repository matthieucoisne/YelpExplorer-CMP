package cmp.yelpexplorer.features.business.presentation.businesslist

sealed interface BusinessListViewState {
    data object ShowLoading : BusinessListViewState
    data class ShowBusinessList(val businessList: BusinessListUiModel) : BusinessListViewState
    data class ShowError(val error: String) : BusinessListViewState
}
