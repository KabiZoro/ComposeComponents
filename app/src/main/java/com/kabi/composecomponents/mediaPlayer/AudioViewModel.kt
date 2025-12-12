package com.kabi.composecomponents.mediaPlayer

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioViewModel(
    application: Application
) : AndroidViewModel(application) {

    val player = ExoPlayer.Builder(application).build()

    var audioFiles by mutableStateOf(listOf<AudioItem>())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            audioFiles = scanLocalAudioFiles(application)
        }
    }

    fun playSong(item: AudioItem) {
        val mediaItem = MediaItem.fromUri(item.uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}