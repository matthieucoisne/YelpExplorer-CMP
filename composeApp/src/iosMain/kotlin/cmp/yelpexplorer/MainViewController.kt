package cmp.yelpexplorer

import androidx.compose.ui.window.ComposeUIViewController
import cmp.yelpexplorer.core.injection.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
