package com.gotneb.lied.music_player.data.providers

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import com.google.common.collect.ImmutableList
import com.gotneb.lied.R
import com.gotneb.lied.music_player.data.services.MusicPlayerService.Companion.CHANNEL_ID
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat

@UnstableApi
class CustomMediaNotificationProvider(
    private val context: Context
) : MediaNotification.Provider {
    override fun createNotification(
        mediaSession: MediaSession,
        mediaButtonPreferences: ImmutableList<CommandButton>,
        actionFactory: MediaNotification.ActionFactory,
        onNotificationChangedCallback: MediaNotification.Provider.Callback
    ): MediaNotification {
        val player = mediaSession.player
        val metadata = player.mediaMetadata

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(metadata.title ?: "Unknown Title")
            .setContentText(metadata.artist ?: "Unknown Artist")
            .setSubText(metadata.albumTitle ?: "Unknown Album")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(metadata.artworkData?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size)
            })
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setOnlyAlertOnce(true)

        // Add actions from command buttons
        val compactViewIndices = mutableListOf<Int>()
        mediaButtonPreferences.forEachIndexed { index, commandButton ->
            val action = actionFactory.createMediaAction(
                mediaSession,
                IconCompat.createWithResource(context, R.drawable.play),
                commandButton.displayName,
                commandButton.playerCommand
            )
            builder.addAction(action)
            if (index < 3) compactViewIndices.add(index)
        }

        builder.setStyle(
            MediaStyleNotificationHelper.MediaStyle(mediaSession)
                .setShowActionsInCompactView(*compactViewIndices.toIntArray())
        )

        val notification = builder.build()
        return MediaNotification(101, notification)
    }

    override fun handleCustomCommand(
        session: MediaSession,
        action: String,
        extras: Bundle
    ): Boolean = false
}