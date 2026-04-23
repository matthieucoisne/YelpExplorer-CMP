package cmp.yelpexplorer.core.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun NavBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // On iOS, back navigation is typically handled by the system's swipe gesture 
    // or a back button in the UI, which calls onBack directly.
}
