package cmp.yelpexplorer.core.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun NavBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // Desktop doesn't have a system back button by default.
}
