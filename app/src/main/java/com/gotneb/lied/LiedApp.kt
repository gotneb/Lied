package com.gotneb.lied

import android.app.Application
import com.gotneb.lied.music_player.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class LiedApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LiedApp)
            androidLogger()

            modules(appModule)
        }
    }
}