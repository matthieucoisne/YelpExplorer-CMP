package cmp.yelpexplorer.features.business.presentation.businesslist

sealed interface BusinessListViewState {
    data object ShowLoading : BusinessListViewState
    data class ShowBusinessList(val businessList: BusinessListUiModel) : BusinessListViewState
    data object ShowError : BusinessListViewState
}
