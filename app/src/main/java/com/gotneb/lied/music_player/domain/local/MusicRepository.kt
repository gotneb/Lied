package com.gotneb.lied.music_player.domain.local

import com.gotneb.lied.music_player.domain.model.Music

interface MusicRepository {
    fun getMusicList(): List<Music>?
}