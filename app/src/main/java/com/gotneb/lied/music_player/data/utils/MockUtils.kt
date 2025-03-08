package com.gotneb.lied.music_player.data.utils

import com.gotneb.lied.music_player.domain.model.Music
import com.gotneb.lied.R

object MockUtils {
    internal fun getMusics(): List<Music> {
        val resId = R.drawable.music_cover_placeholder
        return listOf(
            Music(1, "Bohemian Rhapsody", "Queen", 254000, true, resId),
            Music(2, "Shape of You", "Ed Sheeran", 133000, false, resId),
            Music(3, "Billie Jean", "Michael Jackson", 194000, true, resId),
            Music(4, "Smells Like Teen Spirit", "Nirvana", 301000, false, resId),
            Music(5, "Rolling in the Deep", "Adele", 228000, true, resId),
            Music(6, "Blinding Lights", "The Weeknd", 200000, false, resId),
            Music(7, "Uptown Funk", "Mark Ronson ft. Bruno Mars", 270000, false, resId),
            Music(8, "Hotel California", "Eagles", 390000, true, resId),
            Music(9, "Hey Jude", "The Beatles", 131000, true, resId),
            Music(10, "Someone Like You", "Adele", 185000, false, resId),
        )
    }
}