package com.kabi.composecomponents.record.audio.recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}