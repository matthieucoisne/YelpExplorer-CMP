package cmp.yelpexplorer.features.business.presentation.businesslist

sealed interface BusinessListScreenEvent {
    data class OnBusinessClicked(val business: BusinessUiModel) : BusinessListScreenEvent
}
