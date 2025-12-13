package com.kabi.composecomponents.media_metadata

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun getAudioFileUris(context: Context): List<Uri>{

    val uris = mutableListOf<Uri>()
    val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Audio.Media._ID)

    context.contentResolver.query(
        collection,
        projection,
        null,
        null,
        null
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        while (cursor.moveToNext()){
            val id = cursor.getLong(idColumn)
            val contentUri: Uri = ContentUris.withAppendedId(
                collection,
                id
            )
            uris.add(contentUri)
        }
    }
    return uris
}