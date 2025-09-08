package com.gotneb.lied.music_player.data.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import com.gotneb.lied.R

class MusicPlayerService : Service() {

    private var mediaSession: MediaSession? = null
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // It's called when another Activity sends an intent for my service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.PLAY_PAUSE.toString() -> {
                // TODO()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    @OptIn(UnstableApi::class)
    private fun createNotification(): Notification {
        val style = MediaStyleNotificationHelper.MediaStyle(mediaSession!!)
            .setShowActionsInCompactView(0, 1, 2)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("No track selected")
            .setContentText(if (isPlaying) "Playing" else "Paused")
            .addAction(R.drawable.skip_inward, "Prev", createPrevIntent())
            .addAction(
                if (isPlaying) R.drawable.pause else R.drawable.play,
                if (isPlaying) "Pause" else "Play",
                createPlayPauseIntent()
            )
            .addAction(R.drawable.skip_forward, "Next", createNextIntent())
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(style)
            .setOngoing(true)
            .build()
    }

    private fun createPlayPauseIntent(): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            action = "PLAY_PAUSE"
        }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createPrevIntent(): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            action = "PREV"
        }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNextIntent(): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply {
            action = "NEXT"
        }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    enum class Actions {
        START,
        PLAY_PAUSE,
    }

    companion object {
        private const val NOTIFICATION_ID = 101
        const val NAME = "Music and Audio"
        const val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
        const val CHANNEL_ID = "music_player_channel"

//        fun startService(context: Context, trackTitle: String) {
//            val intent = Intent(context, MusicPlayerService::class.java).apply {
//                action = "UPDATE_TRACK"
//                putExtra("track_title", trackTitle)
//            }
//            ContextCompat.startForegroundService(context, intent)
//        }

        fun togglePlayPause(context: Context) {
            val intent = Intent(context, MusicPlayerService::class.java).apply {
                action = "PLAY_PAUSE"
            }
            context.startService(intent)
        }
    }
}