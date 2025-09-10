package com.gotneb.lied.music_player.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.gotneb.lied.music_player.domain.local.MusicRepository
import com.gotneb.lied.music_player.domain.model.Music
import com.gotneb.lied.R

class MusicRepositoryImpl(
    private val contentResolver: ContentResolver
) : MusicRepository {

    override fun getMusicList(): List<Music>? {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID // added to get album art
        )

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID) // added

            val musicList = mutableListOf<Music>()

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artistName = cursor.getString(artistColumn)
                val duration = cursor.getInt(durationColumn)
                val albumId = cursor.getLong(albumIdColumn) // added

                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                //  works on modern Android
                val albumArtUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )
                // Create MediaItem for player/notifications
                val metadata = MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artistName)
                    .setAlbumTitle("Unknown Album") // optional, could query album name
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMediaMetadata(metadata)
                    .build()

                musicList.add(
                    Music(
                        id = id,
                        uri = uri,
                        mediaItem = mediaItem,
                        name = title,
                        singer = artistName,
                        duration = duration,
                        isFavorite = false,
                        coverRes = R.drawable.music_cover_placeholder,
                        albumCover = albumArtUri.toString(),
                    )
                )

                println("[Music] -> $title - $artistName | [Uri] -> $uri | [AlbumArt] -> ${albumArtUri.toString()}")
            }

            return musicList
        }

        println("There's no musics...")
        return null
    }
}
