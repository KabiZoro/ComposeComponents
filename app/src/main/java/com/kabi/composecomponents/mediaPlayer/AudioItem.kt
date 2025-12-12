package com.kabi.composecomponents.mediaPlayer

import android.net.Uri

data class AudioItem(
    val id: Long,
    val title: String,
    val artist: String,
    val uri: Uri
)

