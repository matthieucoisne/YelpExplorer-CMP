package cmp.yelpexplorer

import android.app.Application
import cmp.yelpexplorer.core.injection.initKoin
import org.koin.android.ext.koin.androidContext

class YelpExplorer : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@YelpExplorer)
        }
    }
}
