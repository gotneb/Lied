package com.gotneb.lied.music_player.domain.model

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.media3.common.MediaItem

@Immutable
data class Music(
    val id: Long,
    val uri: Uri?,
    val mediaItem: MediaItem?,
    val name: String,
    val singer: String,
    val duration: Int,
    val isFavorite: Boolean,
    @DrawableRes
    val coverRes: Int,
)
