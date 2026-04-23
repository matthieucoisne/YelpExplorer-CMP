package cmp.yelpexplorer.core.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun NavBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
)
