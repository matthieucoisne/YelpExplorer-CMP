package cmp.yelpexplorer.features.business.presentation.businessdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.BusinessDetailsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val businessDetailsUseCase: BusinessDetailsUseCase,
    private val businessDetailsMapper: BusinessDetailsMapper,
) : ViewModel() {

    private val businessId = savedStateHandle.getStateFlow<String?>("businessId", null)

    val viewState = businessId.filterNotNull().flatMapLatest {
        businessDetailsUseCase.execute(businessId = it)
    }.map<Business, BusinessDetailsViewState> {
        BusinessDetailsViewState.ShowBusinessDetails(
            businessDetails = businessDetailsMapper.map(it),
        )
    }.catch {
        emit(BusinessDetailsViewState.ShowError)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = BusinessDetailsViewState.ShowLoading
    )
}
