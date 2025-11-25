package cmp.yelpexplorer.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Red900 = Color(0xFFB71C1C)
val Charcoal = Color(0xFF333333)
val WhiteSmoke = Color(0xFFF5F5F5)

val darkColorScheme = darkColorScheme(
    primary = Red900,
    onPrimary = Color.White,
    surface = Color.DarkGray,
    surfaceTint = Color.DarkGray,
    background = Charcoal,
)

val lightColorScheme = lightColorScheme(
    primary = Red900,
    onPrimary = Color.White,
    surface = WhiteSmoke,
    surfaceTint = WhiteSmoke,
    background = Color.White
)
