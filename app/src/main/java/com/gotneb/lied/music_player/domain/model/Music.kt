package com.gotneb.lied.music_player.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class Music(
    val id: String,
    val name: String,
    val singer: String,
    val duration: Int,
    val isFavorite: Boolean,
    @DrawableRes
    val coverRes: Int,
)
