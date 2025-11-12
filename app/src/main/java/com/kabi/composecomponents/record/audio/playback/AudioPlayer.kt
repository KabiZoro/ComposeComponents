package com.kabi.composecomponents.record.audio.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}