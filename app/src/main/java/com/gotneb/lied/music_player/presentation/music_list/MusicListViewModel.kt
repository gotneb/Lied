package com.gotneb.lied.music_player.presentation.music_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.gotneb.lied.music_player.domain.local.MusicRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicListViewModel(
    private val repository: MusicRepository,
    private val player: ExoPlayer,
) : ViewModel() {

    companion object {
        val TAG = MusicListViewModel::class.simpleName!!
    }

    private val _state = MutableStateFlow(MusicListState())
    val state = _state.asStateFlow()

    private val _events = Channel<MusicListEvent>()
    val events = _events.receiveAsFlow()

    private var progressUpdateJob: Job? = null

    init {
        loadMusicFromDevice()
        player.playWhenReady = true
        Log.d(TAG, "State | Player prepared")
    }

    fun onAction(action: MusicListAction) {
        when (action) {
            MusicListAction.OnPermissionDenied -> {
                _state.update {
                    it.copy(
                        isPermissionGranted = false,
                        errorMessage = "Permission to access music storage denied.",
                    )
                }
            }

            MusicListAction.OnPermissionGranted -> {
                _state.update {
                    it.copy(
                        isPermissionGranted = true,
                        errorMessage = null
                    )
                }
            }

            is MusicListAction.OnMusicClick -> {
                _state.value.musicList
                    .find { music -> music.id == action.id }
                    ?.let { music ->
                        // Same music, nothing needs to be changed
                        if (_state.value.currentMusic?.id == music.id) {
                            return
                        }

                        _state.update {
                            it.copy(
                                currentMusic = music,
                                isPlaying = true,
                                totalDuration = music.duration.toLong()
                            )
                        }

                        player.setMediaItem(music.mediaItem!!)
                        player.prepare()
                        player.play()
                        startPlaybackProgressUpdates()
                    }
            }

            is MusicListAction.OnFavoriteMusicClick -> TODO()
            is MusicListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
            }

            MusicListAction.OnSearchClick -> TODO()
            MusicListAction.OnShuffleClick -> TODO()
            MusicListAction.OnNextMusicClick -> TODO()
            MusicListAction.OnPauseMusicClick -> {
                Log.d(TAG, "onAction | Pause music clicked")
                player.pause()
                _state.update { it.copy(isPlaying = false) }
                pausePlaybackProgressUpdates()
            }

            MusicListAction.OnPlayMusicClick -> {
                Log.d(TAG, "onAction | Pause music clicked")
                player.play()
                _state.update { it.copy(isPlaying = true) }
                resumePlaybackProgressUpdates()
            }

            MusicListAction.OnPreviousClick -> TODO()
            MusicListAction.OnRepeatClick -> TODO()
            MusicListAction.OnGoBackClick -> {
                viewModelScope.launch {
                    _events.send(MusicListEvent.OnGoBackClick)
                }
            }

            MusicListAction.OnStartProgressUpdates -> startPlaybackProgressUpdates()
            MusicListAction.OnStopProgressUpdates -> stopPlaybackProgressUpdates()
        }
    }

    private fun startPlaybackProgressUpdates() {
        progressUpdateJob = viewModelScope.launch {
            while (true) {
                Log.d(TAG, "startPlaybackProgressUpdates | Current position: ${player.currentPosition}")
                if (player.isPlaying) {
                    _state.update { it.copy(currentDuration = player.currentPosition) }
                }
                delay(1000L)
            }
        }
    }

    /*
     * FIX: Stop doesn't stop the progressUpdateJob,
     * it seems many coroutines are being created when a music is played
     */
    private fun stopPlaybackProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
        Log.d(TAG, "stopPlaybackProgressUpdates | Progress updates stopped")
    }

    private fun pausePlaybackProgressUpdates() {
        stopPlaybackProgressUpdates()
    }

    private fun resumePlaybackProgressUpdates() {
        if (player.isPlaying) {
            startPlaybackProgressUpdates()
        }
    }

    fun loadMusicFromDevice() {
        Log.d(TAG, "loadMusicFromDevice | Loading music...")
        repository.getMusicList()?.let { musics ->
            _state.update { it.copy(musicList = musics) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        Log.d(TAG, "onCleared | Player released")
    }
}