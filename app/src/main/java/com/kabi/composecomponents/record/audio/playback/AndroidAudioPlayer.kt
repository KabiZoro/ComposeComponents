package com.kabi.composecomponents.record.audio.playback

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this // store the MediaPlayer instance
            start() // start playing the file
        }
    }

    override fun stop() {
        player?.stop() // stop the MediaPlayer instance
        player?.release() // release the MediaPlayer resources
        player = null
    }
}