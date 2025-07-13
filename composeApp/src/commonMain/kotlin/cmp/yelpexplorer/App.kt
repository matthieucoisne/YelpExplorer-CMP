package cmp.yelpexplorer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cmp.yelpexplorer.core.navigation.Routes
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsScreen
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListScreen

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.BusinessList.route,
    ) {
        composable(
            route = Routes.BusinessList.route
        ) {
            BusinessListScreen(
                modifier = Modifier.fillMaxSize(),
                onBusinessClicked = { businessId ->
                    navController.navigate(
                        route = Routes.BusinessDetails.createRoute(businessId)
                    )
                }
            )
        }

        composable(
            route = Routes.BusinessDetails.route,
            arguments = Routes.BusinessDetails.navArguments
        ) {
            BusinessDetailsScreen(
                modifier = Modifier.fillMaxSize(),
                onBackPressed = { navController.navigateUp() }
            )
        }
    }
}
