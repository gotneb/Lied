package com.gotneb.lied.music_player.presentation.music_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotneb.lied.music_player.domain.local.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MusicListViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    companion object {
        const val TAG = "MusicListViewModel"
    }

    private val _state = MutableStateFlow(MusicListState())
    val state = _state
        .onStart { loadMusic() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            MusicListState(),
        )

    fun onAction(action: MusicListAction) {
        when (action) {
            MusicListAction.OnPermissionDenied -> {
                _state.update { it.copy(isPermissionGranted = false) }
            }
            MusicListAction.OnPermissionGranted -> {
                _state.update { it.copy(
                    isPermissionGranted = true,
                    errorMessage = "Permission denied"
                ) }
            }

            is MusicListAction.OnMusicClick -> TODO()
            is MusicListAction.OnMusicFavoriteClick -> TODO()
            is MusicListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
            }

            MusicListAction.OnSearchClick -> TODO()
            MusicListAction.OnShuffleClick -> TODO()
        }
    }

    fun loadMusic() {
        println("loadMusic: Getting musics...")
        repository.getMusicList()?.let { musics ->
            _state.update { it.copy(musics = musics) }
        }
    }
}