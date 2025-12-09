package com.kabi.composecomponents.externalMediaFiles

import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat

/**
 * MediaStore -> It contains all the Metadata of our android
 * Metadata -> Its the type of media and it may be Image, Video, Audio
 * ContentProvider -> It provides the Metadata we want from the MediaStore
 * */

class MediaReader(
    private val context: Context
) {
    fun getAllMediaFiles(): List<MediaFile> {
        val skipQuery = if (Build.VERSION.SDK_INT <= 32){
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        } else false

        if (skipQuery){
            return emptyList()
        }

        val mediaFiles = mutableListOf<MediaFile>()
        val queryUri = if (Build.VERSION.SDK_INT >= 29) {
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID, // id of each item
            MediaStore.Files.FileColumns.DISPLAY_NAME, // display name of each item
            MediaStore.Files.FileColumns.MIME_TYPE // mime type of each item, audio/mp3 or video/webm
        )

        context.contentResolver.query(
            queryUri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Files.FileColumns._ID
            )
            val nameColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Files.FileColumns.DISPLAY_NAME
            )
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Files.FileColumns.MIME_TYPE
            )
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val mimeType = cursor.getString(mimeTypeColumn)

                if (name != null && mimeType != null) {
                    val contentUri = ContentUris.withAppendedId(
                        queryUri,
                        id
                    )
                    val mediaType = when {
                        mimeType.startsWith("audio/") -> MediaType.AUDIO
                        mimeType.startsWith("video/") -> MediaType.VIDEO
                        else -> MediaType.IMAGE
                    }

                    mediaFiles.add(
                        MediaFile(
                            uri = contentUri,
                            name = name,
                            type = mediaType
                        )
                    )
                }
            }
        }
        return mediaFiles.toList()
    }
}