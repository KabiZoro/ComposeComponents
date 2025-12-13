package com.kabi.composecomponents.media_metadata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kabi.composecomponents.R

@Composable
fun AudioMetadataScreen(
    viewmodel: AudioMetadataViewmodel = viewModel()
) {
    val audioList by viewmodel.audioMetadata

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (audioList.isEmpty()){
            item { Text(text = "Scanning for audio files...") }
        } else {
            items(audioList) { metadata ->
                MetadataListItem(metadata)
            }
        }
    }

}

@Composable
fun MetadataListItem(metadata: AudioMetadata) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = metadata.image,
            contentDescription = "Album art for ${metadata.title}",
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = metadata.title
        )
        Text(
            text = "Artist : ${metadata.artist}"
        )
        Text(
            text = "Duration : ${metadata.durationMs / 1000}s"
        )
    }
}