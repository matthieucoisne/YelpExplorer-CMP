package cmp.yelpexplorer.features.business.presentation.businesslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmp.yelpexplorer.features.business.domain.model.Business
import cmp.yelpexplorer.features.business.domain.usecase.BusinessListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class BusinessListViewModel(
    private val businessListUseCase: BusinessListUseCase,
    private val businessListMapper: BusinessListMapper,
    mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val searchParams = MutableStateFlow(
        SearchParams(
            term = "sushi",
            location = "montreal",
            sortBy = "rating",
            limit = 20,
        )
    )

    val viewState: StateFlow<BusinessListViewState> = searchParams
        .debounce(300L)
        .flatMapLatest { params ->
            businessListUseCase.execute(
                term = params.term,
                location = params.location,
                sortBy = params.sortBy,
                limit = params.limit,
            )
        }.map<List<Business>, BusinessListViewState> {
            BusinessListViewState.ShowBusinessList(
                businessList = businessListMapper.map(it)
            )
        }.catch {
            emit(BusinessListViewState.ShowError)
        }.stateIn(
            scope = CoroutineScope(viewModelScope.coroutineContext + mainDispatcher),
            started = SharingStarted.Lazily,
            initialValue = BusinessListViewState.ShowLoading
        )
}

private data class SearchParams(
    val term: String,
    val location: String,
    val sortBy: String,
    val limit: Int,
)
