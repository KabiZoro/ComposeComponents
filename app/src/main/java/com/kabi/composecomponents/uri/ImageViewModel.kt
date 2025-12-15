package com.kabi.composecomponents.uri

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabi.composecomponents.mediaPlayer.scanLocalAudioFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel: ViewModel() {

    var images by mutableStateOf(emptyList<Image>())
        private set

    fun updateImages(images: List<Image>){
        this.images = images
    }

}