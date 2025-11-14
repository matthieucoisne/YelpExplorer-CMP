package cmp.yelpexplorer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cmp.yelpexplorer.core.injection.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "YelpExplorer-CMP",
    ) {
        App()
    }
}