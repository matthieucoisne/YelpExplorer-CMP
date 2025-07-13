package cmp.yelpexplorer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cmp.yelpexplorer.core.injection.initKoin
import cmp.yelpexplorer.core.theme.YelpExplorerTheme

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "YelpExplorer-CMP",
    ) {
        YelpExplorerTheme(darkTheme = true) {
            App()
        }
    }
}