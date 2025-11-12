@file:OptIn(ExperimentalPermissionsApi::class)

package com.kabi.composecomponents.record.audio

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kabi.composecomponents.R
import com.kabi.composecomponents.record.audio.playback.AndroidAudioPlayer
import com.kabi.composecomponents.record.audio.recorder.AndroidAudioRecorder
import com.kabi.composecomponents.ui.theme.CodingGreen
import com.kabi.composecomponents.ui.theme.CodingGrey
import java.io.File

@Composable
fun AudioMain(
    context: Context = LocalContext.current
) {

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    val recorder by remember { mutableStateOf(AndroidAudioRecorder(context)) }
    val player by remember { mutableStateOf(AndroidAudioPlayer(context)) }
    var audioFile by remember { mutableStateOf<File?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = CodingGrey
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!permissionState.status.isGranted) {
                Text(
                    text = "Audio recording permission is required to record audio.",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // start and stop recording
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        File(context.cacheDir, "audio.mp3").also {
                            recorder.start(it)
                            audioFile = it
                        }
                        Toast.makeText(
                            context,
                            "Recording Started",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    enabled = permissionState.status.isGranted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodingGreen
                    )
                ) {
                    Text(
                        text = "Start Recording",
                        color = CodingGrey,
                        fontSize = 24.sp
                    )
                }

                Button(
                    onClick = {
                        recorder.stop()
                        Toast.makeText(
                            context,
                            "Recording Stopped",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodingGreen
                    )
                ) {
                    Text(
                        text = "Stop Recording",
                        color = CodingGrey,
                        fontSize = 24.sp
                    )
                }
            }

            // play and stop button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        player.playFile(audioFile ?: return@IconButton)
                        Toast.makeText(
                            context,
                            "Playing",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(elevation = 4.dp)
                        .background(CodingGreen),
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play"
                    )
                }

                IconButton(
                    onClick = {
                        player.stop()
                        Toast.makeText(
                            context,
                            "Stopped",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(elevation = 4.dp)
                        .background(CodingGreen),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.stop),
                        contentDescription = "Stop",
                        modifier = Modifier
                            .size(14.dp)
                    )
                }
            }

        }

    }
}

@Preview
@Composable
private fun AudioMainPreview() {
    val context = LocalContext.current
    AudioMain(context)
}