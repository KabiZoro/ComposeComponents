package com.kabi.composecomponents.mediaPlayer

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun AudioPlayerScreen(viewModel: AudioViewModel = viewModel()) {
    val context = LocalContext.current
    val audioFiles = viewModel.audioFiles

    // Request permission (simplified for brevity)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Handle permission result
    }

    val permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    LaunchedEffect(Unit) {
        launcher.launch(permission)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(audioFiles) { audioItem ->
            AudioFileListItem(audioItem = audioItem) {
                viewModel.playSong(audioItem)
            }
        }
    }

    PlaybackControls(viewModel.player)
}

@Composable
fun AudioFileListItem(audioItem: AudioItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = audioItem.title, style = MaterialTheme.typography.titleMedium)
            Text(text = audioItem.artist, style = MaterialTheme.typography.bodySmall)
        }
    }
    HorizontalDivider()
}

@Composable
fun PlaybackControls(player: ExoPlayer) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
                useController = true
                setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Black)
    )
}