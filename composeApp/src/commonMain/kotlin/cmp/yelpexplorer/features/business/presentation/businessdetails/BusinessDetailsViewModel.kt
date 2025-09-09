package cmp.yelpexplorer.features.business.presentation.businessdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmp.yelpexplorer.features.business.domain.usecase.BusinessDetailsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.jetbrains.compose.resources.getString
import yelpexplorer_cmp.composeapp.generated.resources.Res
import yelpexplorer_cmp.composeapp.generated.resources.error_something_went_wrong

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val businessDetailsUseCase: BusinessDetailsUseCase,
    private val businessDetailsMapper: BusinessDetailsMapper,
) : ViewModel() {

    private val businessId = savedStateHandle.getStateFlow<String?>("businessId", null)

    val viewState = businessId.filterNotNull().flatMapLatest {
        businessDetailsUseCase.execute(businessId = it)
    }.map {
        BusinessDetailsViewState.ShowBusinessDetails(
            businessDetails = businessDetailsMapper.map(it),
        )
    }.catch {
        BusinessDetailsViewState.ShowError(
            error = it.message ?: getString(Res.string.error_something_went_wrong),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = BusinessDetailsViewState.ShowLoading
    )
}
