package com.kabi.composecomponents.record.audio.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class AndroidAudioRecorder(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) // recorder audio from Mic of Device
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // recorder audio in MPEG_4 format
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // encoder audio in AAC format
            setOutputFile(FileOutputStream(outputFile).fd) // output file

            prepare() // prepare to record
            start() // start record

            recorder = this // record from this MediaRecorder
        }
    }

    override fun stop() {
        recorder?.stop() // stop record
        recorder?.reset() // reset recorder
        recorder = null // set recorder to null
    }

}