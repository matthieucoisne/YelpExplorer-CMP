package cmp.yelpexplorer.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cmp.yelpexplorer.features.business.presentation.businessdetails.BusinessDetailsScreen
import cmp.yelpexplorer.features.business.presentation.businesslist.BusinessListScreen

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.BusinessList.route,
    ) {
        composable(
            route = Routes.BusinessList.route
        ) {
            BusinessListScreen(
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
                onBackPressed = { navController.navigateUp() }
            )
        }
    }
}
