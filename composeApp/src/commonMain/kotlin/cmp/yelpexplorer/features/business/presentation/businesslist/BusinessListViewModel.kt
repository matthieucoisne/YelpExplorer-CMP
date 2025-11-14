package cmp.yelpexplorer.features.business.presentation.businesslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessListViewModel(
    private val businessListUseCase: BusinessListUseCase,
    private val businessListMapper: BusinessListMapper,
) : ViewModel() {

    private val term = MutableStateFlow("sushi")
    private val location = MutableStateFlow("montreal")
    private val sortBy = MutableStateFlow("rating")
    private val limit = MutableStateFlow(20)

    val viewState: StateFlow<BusinessListViewState> = combine(
        term,
        location,
        sortBy,
        limit,
    ) { term, location, sortBy, limit ->
        businessListUseCase.execute(
            term = term,
            location = location,
            sortBy = sortBy,
            limit = limit,
        )
    }.flatMapLatest {
        it
    }.map<List<Business>, BusinessListViewState> {
        BusinessListViewState.ShowBusinessList(
            businessList = businessListMapper.map(it)
        )
    }.catch {
        emit(BusinessListViewState.ShowError)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = BusinessListViewState.ShowLoading
    )
}
