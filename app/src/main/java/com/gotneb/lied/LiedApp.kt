package com.gotneb.lied

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.gotneb.lied.music_player.data.services.MusicPlayerService
import com.gotneb.lied.music_player.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class LiedApp: Application() {

    override fun onCreate() {
        super.onCreate()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MusicPlayerService.CHANNEL_ID,
                MusicPlayerService.NAME,
                MusicPlayerService.IMPORTANCE,
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        startKoin {
            androidContext(this@LiedApp)
            androidLogger()

            modules(appModule)
        }
    }
}