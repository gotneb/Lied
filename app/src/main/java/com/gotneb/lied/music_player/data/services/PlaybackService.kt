package com.gotneb.lied.music_player.data.services

import androidx.media3.common.AudioAttributes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.collect.ImmutableList
import com.gotneb.lied.R
import com.gotneb.lied.music_player.data.providers.CustomMediaNotificationProvider

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    // Create your Player and MediaSession in the onCreate lifecycle event
    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        this.setMediaNotificationProvider(CustomMediaNotificationProvider(this))

        val player = ExoPlayer
            .Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()

        val prevButton = CommandButton.Builder(CommandButton.ICON_PREVIOUS)
            .setPlayerCommand(Player.COMMAND_SEEK_TO_PREVIOUS)
            .setDisplayName("Previous")
            .setCustomIconResId(R.drawable.skip_inward)
            .build()

        val playPauseButton = CommandButton.Builder(CommandButton.ICON_PLAY)
            .setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
            .setDisplayName("Play/Pause")
            .setCustomIconResId(R.drawable.play)
            .build()

        val nextButton = CommandButton.Builder(CommandButton.ICON_NEXT)
            .setPlayerCommand(Player.COMMAND_SEEK_TO_NEXT)
            .setDisplayName("Next")
            .setCustomIconResId(R.drawable.skip_forward)
            .build()

        mediaSession = MediaSession.Builder(this, player)
            .setMediaButtonPreferences(ImmutableList.of(prevButton, playPauseButton, nextButton))
            .build()
    }

    // This example always accepts the connection request
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}