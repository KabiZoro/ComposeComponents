package com.kabi.composecomponents.media_metadata

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
fun retrieveMetadataFromUri(
    context: Context,
    uri: Uri
): AudioMetadata? {

    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)

        val title = retriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            ?: uri.lastPathSegment
            ?: "Unknown Title"

        val artist = retriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            ?: "Unknown Artist"

        val image = retriever.embeddedPicture ?: ByteArray(0)

        val durationString = retriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        val durationMs = durationString?.toLongOrNull() ?: 0L

        AudioMetadata(
            title = title,
            artist = artist,
            durationMs = durationMs,
            uri = uri,
            image = image
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }

}