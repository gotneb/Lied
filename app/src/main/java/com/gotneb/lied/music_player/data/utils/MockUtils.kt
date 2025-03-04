package com.gotneb.lied.music_player.data.utils

import com.gotneb.lied.music_player.domain.model.Music
import com.gotneb.lied.R

object MockUtils {
    internal fun getMusics(): List<Music> {
        val resId = R.drawable.no_cover_music
        return listOf(
            Music(1, "Bohemian Rhapsody", "Queen", 354, true, resId),
            Music(2, "Shape of You", "Ed Sheeran", 233, false, resId),
            Music(3, "Billie Jean", "Michael Jackson", 294, true, resId),
            Music(4, "Smells Like Teen Spirit", "Nirvana", 301, false, resId),
            Music(5, "Rolling in the Deep", "Adele", 228, true, resId),
            Music(6, "Blinding Lights", "The Weeknd", 200, false, resId),
            Music(7, "Uptown Funk", "Mark Ronson ft. Bruno Mars", 270, false, resId),
            Music(8, "Hotel California", "Eagles", 390, true, resId),
            Music(9, "Hey Jude", "The Beatles", 431, true, resId),
            Music(10, "Someone Like You", "Adele", 285, false, resId),
        )
    }
}