package com.gotneb.lied.music_player.presentation.music_list

import android.app.Application
import android.content.ComponentName
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.gotneb.lied.music_player.data.services.PlaybackService
import com.gotneb.lied.music_player.domain.local.MusicRepository
import com.gotneb.lied.music_player.domain.model.Music
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicListViewModel(
    private val repository: MusicRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(MusicListState())
    val state = _state.asStateFlow()

    private val _events = Channel<MusicListEvent>()
    val events = _events.receiveAsFlow()

    private var progressUpdateJob: Job? = null

    private lateinit var player: Player

    init {
        Log.d(TAG, "init | START")
        val sessionToken = SessionToken(
            application,
            ComponentName(application, PlaybackService::class.java)
        )
        val controllerFuture = MediaController.Builder(application, sessionToken).buildAsync()
        controllerFuture.addListener({
            player = controllerFuture.get()
            Log.d(TAG, "init | Player loaded")

            loadMusicFromDevice()
            sortMusicList()
            player.playWhenReady = true
            Log.d(TAG, "init | Player prepared")
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())
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
                _state.value.originalPlaylist
                    .find { music -> music.id == action.id }
                    ?.let { music ->
                        // Same music, nothing needs to be changed
                        if (_state.value.currentMusic?.id == music.id) {
                            return
                        }
                        playMusic(music)
                    }
            }

            is MusicListAction.OnFavoriteMusicClick -> TODO()
            is MusicListAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
            }

            MusicListAction.OnSearchClick -> TODO()
            MusicListAction.OnShuffleClick -> toggleShuffle()
            MusicListAction.OnNextMusicClick -> playNextMusic()
            MusicListAction.OnPreviousClick -> playPreviousMusic()
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

            MusicListAction.OnRepeatClick -> repeatMusic()
            MusicListAction.OnGoBackClick -> {
                viewModelScope.launch {
                    _events.send(MusicListEvent.OnGoBackClick)
                }
            }

            MusicListAction.OnStartProgressUpdates -> startPlaybackProgressUpdates()
            MusicListAction.OnStopProgressUpdates -> stopPlaybackProgressUpdates()
            is MusicListAction.OnSeekDurationMusic -> {
                Log.d(TAG, "ProgressAudioBar | onValueChange: ${action.duration}")
                player.seekTo(action.duration)
                _state.update { it.copy(currentDuration = action.duration) }
            }

            MusicListAction.OnPlayRandomMusicClick -> playRandomMusic()
        }
    }

    private fun playMusic(music: Music) {
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

    private fun playNextMusic() {
        val index = _state.value.currentPlaylist
            .indexOfFirst { music -> music.id == _state.value.currentMusic!!.id }
        val lastIndex = _state.value.currentPlaylist.lastIndex
        val nextMusic = _state.value.currentPlaylist[if (index == lastIndex) 0 else index + 1]
        playMusic(nextMusic)
    }

    private fun playPreviousMusic() {
        val index = _state.value.currentPlaylist
            .indexOfFirst { music -> music.id == _state.value.currentMusic!!.id }
        val lastIndex = _state.value.currentPlaylist.lastIndex
        val previousMusic = _state.value.currentPlaylist[if (index == 0) lastIndex else index - 1]
        playMusic(previousMusic)
    }

    private fun startPlaybackProgressUpdates() {
        Log.d(TAG, "${::startPlaybackProgressUpdates.name} | Progress updates started")
        progressUpdateJob = viewModelScope.launch {
            while (true) {
                Log.d(
                    TAG,
                    "startPlaybackProgressUpdates | Current position: ${player.currentPosition}"
                )
                if (player.isPlaying) {
                    _state.update { it.copy(currentDuration = player.currentPosition) }
                }
                delay(500L)
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
        Log.d(TAG, "${::stopPlaybackProgressUpdates.name} | Progress updates stopped")
    }

    private fun pausePlaybackProgressUpdates() {
        Log.d(TAG, "${::pausePlaybackProgressUpdates.name} | Progress updates paused")
        stopPlaybackProgressUpdates()
    }

    private fun resumePlaybackProgressUpdates() {
        Log.d(TAG, "${::resumePlaybackProgressUpdates.name} | Progress updates resumed")
        if (player.isPlaying) {
            startPlaybackProgressUpdates()
        }
    }

    fun loadMusicFromDevice() {
        Log.d(TAG, "${::loadMusicFromDevice.name} | Loading music...")
        repository.getMusicList()?.let { musics ->
            _state.update {
                it.copy(
                    originalPlaylist = musics,
                    currentPlaylist = musics,
                )
            }
        }
    }

    private fun sortMusicList() {
        _state.update {
            val sorted = _state.value.originalPlaylist.sortedBy { music -> music.duration }
            it.copy(
                originalPlaylist = sorted,
                currentPlaylist = sorted,
            )
        }
    }

    private fun repeatMusic() = viewModelScope.launch {
        Log.d(TAG, "repeatMusic | Repeat music")
        _state.update { it.copy(isRepeat = !it.isRepeat) }
        if (_state.value.isRepeat) {
            player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            _events.send(MusicListEvent.OnRepeatClick("Repeat ON"))
        } else {
            player.repeatMode = ExoPlayer.REPEAT_MODE_OFF
            _events.send(MusicListEvent.OnRepeatClick("Repeat OFF"))
        }
    }

    // TODO: is this needed?
    private fun togglePlayback() {
        if (player.isPlaying) {
            onAction(MusicListAction.OnPauseMusicClick)
        } else {
            onAction(MusicListAction.OnPlayMusicClick)
        }
    }

    private fun toggleShuffle() {
        Log.d(TAG, "${::toggleShuffle.name} | Toggling music")
        _state.update { current ->
            if (current.isShuffle) {
                // Restore original order
                current.copy(
                    currentPlaylist = current.originalPlaylist,
                    isShuffle = false,
                )
            } else {
                current.copy(
                    currentPlaylist = current.currentPlaylist.shuffled(),
                    isShuffle = true,
                )
            }
        }
    }

    private fun shuffleCurrentPlaylist() {
        Log.d(TAG, "${::shuffleCurrentPlaylist.name} | Shuffling music")
        _state.update {
            it.copy(
                currentPlaylist = it.currentPlaylist.shuffled(),
                isShuffle = true,
            )
        }
    }

    private fun playRandomMusic() {
        Log.d(TAG, "${::playRandomMusic.name} | Playing random music")
        // shuffleCurrentPlaylist()
        val randomIndex = (0 until _state.value.originalPlaylist.size).random()
        playMusic(_state.value.originalPlaylist[randomIndex])
        viewModelScope.launch {
            _events.send(MusicListEvent.GoToMusicPlayer)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        Log.d(TAG, "onCleared | Player released")
    }

    companion object {
        val TAG = MusicListViewModel::class.simpleName!!
    }
}