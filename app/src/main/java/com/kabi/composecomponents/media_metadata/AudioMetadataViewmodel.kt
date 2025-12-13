package com.kabi.composecomponents.media_metadata

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.P)
class AudioMetadataViewmodel(
    application: Application
) : AndroidViewModel(application) {

    private val _audioMetadata = mutableStateOf<List<AudioMetadata>>(emptyList())
    val audioMetadata: State<List<AudioMetadata>> = _audioMetadata

    init {
        loadAudioMetadata()
    }

    private fun loadAudioMetadata() {
        val context = getApplication<Application>()

        viewModelScope.launch(Dispatchers.IO) {
            val uris = getAudioFileUris(context)
            val metadataList = uris.mapNotNull { uri ->
                retrieveMetadataFromUri(context, uri)
            }

            withContext(Dispatchers.Main) {
                _audioMetadata.value = metadataList
            }
        }
    }

}