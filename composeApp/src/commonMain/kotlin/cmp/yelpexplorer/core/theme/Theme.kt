package cmp.yelpexplorer.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val darkColorScheme = darkColorScheme(
    primary = Red900,
    onPrimary = Color.White,
    surface = Color.DarkGray,
    surfaceTint = Color.DarkGray,
    background = Charcoal,
)

private val lightColorScheme = lightColorScheme(
    primary = Red900,
    onPrimary = Color.White,
    surface = WhiteSmoke,
    surfaceTint = WhiteSmoke,
    background = Color.White
)

@Composable
fun YelpExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
