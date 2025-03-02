package com.gotneb.lied.music_player.presentation.music_list

sealed interface MusicListAction {
    object OnPermissionGranted : MusicListAction
    object OnPermissionDenied : MusicListAction

    data class OnMusicClick(val id: Long): MusicListAction
    data class OnMusicFavoriteClick(val id: Long): MusicListAction
    data class OnSearchQueryChange(val query: String): MusicListAction

    data object OnShuffleClick: MusicListAction
    data object OnSearchClick: MusicListAction
}