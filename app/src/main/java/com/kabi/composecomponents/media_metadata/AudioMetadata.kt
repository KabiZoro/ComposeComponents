package com.kabi.composecomponents.media_metadata

import android.net.Uri

data class AudioMetadata(
    val title: String,
    val artist: String,
    val durationMs: Long,
    val uri: Uri,
    val image: ByteArray
)
