package com.gotneb.lied.music_player.data.local

import android.content.ContentResolver
import android.provider.MediaStore
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

                musicList.add(Music(id, name, artistName, duration, false, R.drawable.no_cover_music))
                println("Music -> $name - $artistName")
            }

            return musicList
        }

        println("There's no musics...")
        return null
    }
}