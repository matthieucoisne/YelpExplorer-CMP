package cmp.yelpexplorer.core.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.collect

@Composable
actual fun NavBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    BackHandler(enabled = enabled, onBack = onBack)

    PredictiveBackHandler(enabled = enabled) { progress ->
        try {
            progress.collect()
            onBack()
        } catch (e: Exception) {
            // Gesture cancelled
        }
    }
}
