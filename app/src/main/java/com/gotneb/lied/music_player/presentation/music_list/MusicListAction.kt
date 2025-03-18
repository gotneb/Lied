package com.gotneb.lied.music_player.presentation.music_list

sealed interface MusicListAction {
    object OnPermissionGranted : MusicListAction
    object OnPermissionDenied : MusicListAction

    data object OnStartProgressUpdates : MusicListAction
    data object OnStopProgressUpdates : MusicListAction

    data object OnPlayMusicClick : MusicListAction
    data object OnPauseMusicClick : MusicListAction
    data object OnNextMusicClick : MusicListAction
    data object OnPreviousClick : MusicListAction
    data object OnRepeatClick : MusicListAction
    data object OnShuffleClick: MusicListAction

    data object OnGoBackClick : MusicListAction
    data class OnMusicClick(val id: Long): MusicListAction
    data class OnFavoriteMusicClick(val id: Long): MusicListAction
    data class OnSearchQueryChange(val query: String): MusicListAction

    data object OnSearchClick: MusicListAction
}