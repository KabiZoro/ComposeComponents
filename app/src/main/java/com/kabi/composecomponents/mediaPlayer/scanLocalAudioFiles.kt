package com.kabi.composecomponents.mediaPlayer

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun scanLocalAudioFiles(context: Context): List<AudioItem> {
    val audioList = mutableListOf<AudioItem>()
    val contentResolver = context.contentResolver
    val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    // Columns we want to retrieve
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
    )

    contentResolver.query(
        collection,
        projection,
        null,
        null,
        null
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media._ID
        )
        val titleColumn = cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media.TITLE
        )
        val artistColumn = cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media.ARTIST
        )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val title = cursor.getString(titleColumn) ?: "Unknown Title"
            val artist = cursor.getString(artistColumn) ?: "Unknown Artist"

            val contentUri: Uri = ContentUris.withAppendedId(collection, id)

            audioList.add(AudioItem(id, title, artist, contentUri))
        }
    }
    return audioList
}