package com.gotneb.lied.music_player.presentation.music_list

sealed interface MusicListEvent {
    data object OnGoBackClick : MusicListEvent
    data class OnRepeatClick(val message: String) : MusicListEvent
}