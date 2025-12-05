package cmp.yelpexplorer.features.business.presentation.businessdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.GetBusinessDetailsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BusinessDetailsViewModel(
    businessId: String,
    getBusinessDetailsUseCase: GetBusinessDetailsUseCase,
    private val businessDetailsMapper: BusinessDetailsMapper,
) : ViewModel() {

    val viewState = getBusinessDetailsUseCase(businessId)
        .map<Business, BusinessDetailsViewState> {
            BusinessDetailsViewState.ShowBusinessDetails(
                businessDetails = businessDetailsMapper.map(it),
            )
        }.catch {
            emit(BusinessDetailsViewState.ShowError)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = BusinessDetailsViewState.ShowLoading,
        )
}
