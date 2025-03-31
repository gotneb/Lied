package com.gotneb.lied.music_player.presentation.music_list

import androidx.compose.runtime.Immutable
import com.gotneb.lied.music_player.domain.model.Music

@Immutable
data class MusicListState(
    val searchQuery: String = "",
    val filteredMusics: List<Music> = emptyList(),

    val totalDuration: Long = 120000,
    val currentDuration: Long = 84000, // just for preview :P

    val currentPlaylist: List<Music> = emptyList(),
    val originalPlaylist: List<Music> = emptyList(),
    val currentMusic: Music? = null,

    val isPlaying: Boolean = false,
    val isShuffle: Boolean = false,
    val isRepeat: Boolean = false,

    val isPermissionGranted: Boolean = false,
    val errorMessage: String? = null,
)
