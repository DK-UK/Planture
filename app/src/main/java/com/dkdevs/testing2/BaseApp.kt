package com.dkdevs.testing2

import android.app.Application
import com.dkdevs.testing2.data.module.dataModules
import com.dkdevs.testing2.ui.vm.module.uiModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            this.modules(
                dataModules,
                uiModules
            )
            androidContext(this@BaseApp)
        }
    }
}