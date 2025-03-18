package com.gotneb.lied.music_player.data.utils

import android.media.browse.MediaBrowser
import android.net.Uri
import androidx.media3.common.MediaItem
import com.gotneb.lied.music_player.domain.model.Music
import com.gotneb.lied.R

object MockUtils {
    internal fun getMusics(): List<Music> {
        val resId = R.drawable.music_cover_placeholder

        return listOf(
            Music(1, null, null, "Bohemian Rhapsody", "Queen", 254000, true, resId),
            Music(2, null, null, "Shape of You", "Ed Sheeran", 133000, false, resId),
            Music(3, null, null, "Billie Jean", "Michael Jackson", 194000, true, resId),
            Music(4, null, null, "Smells Like Teen Spirit", "Nirvana", 301000, false, resId),
            Music(5, null, null, "Rolling in the Deep", "Adele", 228000, true, resId),
            Music(6, null, null, "Blinding Lights", "The Weeknd", 200000, false, resId),
            Music(7, null, null, "Uptown Funk", "Mark Ronson ft. Bruno Mars", 270000, false, resId),
            Music(8, null, null, "Hotel California", "Eagles", 390000, true, resId),
            Music(9, null, null, "Hey Jude", "The Beatles", 131000, true, resId),
            Music(10, null, null, "Someone Like You", "Adele", 185000, false, resId),
        )
    }
}