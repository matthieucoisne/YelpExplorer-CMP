package cmp.yelpexplorer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cmp.yelpexplorer.core.navigation.MainNavHost
import cmp.yelpexplorer.core.theme.YelpExplorerTheme

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    YelpExplorerTheme(darkTheme = true) {
        MainNavHost(navController)
    }
}
