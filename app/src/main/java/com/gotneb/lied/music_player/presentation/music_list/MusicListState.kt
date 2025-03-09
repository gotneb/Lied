package com.gotneb.lied.music_player.presentation.music_list

import androidx.compose.runtime.Immutable
import com.gotneb.lied.music_player.domain.model.Music

@Immutable
data class MusicListState(
    val searchQuery: String = "",
    val musics: List<Music> = emptyList(),
    val selectedMusic: Music? = null,
    val filteredMusics: List<Music> = emptyList(),
    val isPermissionGranted: Boolean = false,
    val errorMessage: String? = null,
)
