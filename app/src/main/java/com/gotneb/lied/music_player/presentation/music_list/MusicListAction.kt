package com.gotneb.lied.music_player.presentation.music_list

sealed interface MusicListAction {
    object OnPermissionGranted : MusicListAction
    object OnPermissionDenied : MusicListAction

    data object OnGoBackClick : MusicListAction
    data object OnPlayMusic : MusicListAction
    data object OnPauseMusic : MusicListAction
    data object OnNextClick : MusicListAction
    data object OnPreviousClick : MusicListAction
    data object OnRepeatClick : MusicListAction
    data object OnShuffleClick: MusicListAction

    data class OnMusicClick(val id: Long): MusicListAction
    data class OnMusicFavoriteClick(val id: Long): MusicListAction
    data class OnSearchQueryChange(val query: String): MusicListAction

    data object OnSearchClick: MusicListAction
}