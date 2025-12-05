package cmp.yelpexplorer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import cmp.yelpexplorer.core.theme.YelpExplorerTheme
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsScreen
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsViewModel
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data object BusinessListRoute : NavKey
@Serializable
data class BusinessDetailsRoute(val businessId: String) : NavKey

@Composable
fun App() {
    val backStack = remember { mutableStateListOf<Any>(BusinessListRoute) }

    YelpExplorerTheme(darkTheme = true) {
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider{
                entry<BusinessListRoute> {
                    BusinessListScreen { businessId ->
                        backStack.add(BusinessDetailsRoute(businessId))
                    }
                }
                entry<BusinessDetailsRoute> { route ->
                    val viewModel = koinViewModel<BusinessDetailsViewModel> {
                        parametersOf(route.businessId)
                    }
                    BusinessDetailsScreen(viewModel = viewModel) {
                        backStack.removeLastOrNull()
                    }
                }
            }
        )
    }
}
