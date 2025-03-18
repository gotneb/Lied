package com.gotneb.lied.music_player.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import com.gotneb.lied.music_player.domain.local.MusicRepository
import com.gotneb.lied.music_player.domain.model.Music
import com.gotneb.lied.R

class MusicRepositoryImpl(
    private val contentResolver: ContentResolver
): MusicRepository {

    override fun getMusicList(): List<Music>? {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val filename = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)

            val musicList = mutableListOf<Music>()

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(filename)
                val artistName = cursor.getString(artistColumn)
                val duration = cursor.getInt(duration)
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                val mediaItem = MediaItem.fromUri(uri)

                musicList.add(Music(id, uri, mediaItem, name, artistName, duration, false, R.drawable.music_cover_placeholder))
                println("[Music] -> $name - $artistName | [Uri] -> $uri | [mediaItem] -> $mediaItem")
            }

            return musicList
        }

        println("There's no musics...")
        return null
    }
}