package com.kabi.composecomponents

import android.content.ContentUris
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kabi.composecomponents.mediaPlayer.AudioPlayerScreen
import com.kabi.composecomponents.media_metadata.AudioMetadataScreen
import com.kabi.composecomponents.ui.theme.ComposeComponentsTheme
import com.kabi.composecomponents.uri.Image
import com.kabi.composecomponents.uri.ImageViewModel

// content providers -- https://youtu.be/IVHZpTyVOxU

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
            0
        )

        val images = mutableListOf<Image>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val millisYesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.timeInMillis
        val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
        val selectionArgs = arrayOf(millisYesterday.toString())
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // want images in external storage
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor -> // cursor is used to iterate over larger dataset

            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(Image(id, displayName, uri))
            }
            viewModel.updateImages(images)
        }

        enableEdgeToEdge()
        setContent {
            ComposeComponentsTheme {
                AudioMetadataScreen()
                /*LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(viewModel.images) { image ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = image.uri,
                                contentDescription = null
                            )
                            Text(
                                text = image.name
                            )
                        }
                    }
                }*/
            }
        }
    }
}


// contentUri

/*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // content Uri
        val uri = Uri.parse("android.resource://$packageName/drawable/photo")
        val photoBytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
        println("Screenshot size: ${photoBytes?.size}")

        val file = File(filesDir, "photo.png")
        FileOutputStream(file).use {
            it.write(photoBytes)
        }
        println(file.toUri())

        enableEdgeToEdge()
        setContent {
            ComposeComponentsTheme {

                // content Uri
                val pickImage = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { contentUri ->
                        println(contentUri)
                    }
                )
                Button(
                    onClick = {
                        pickImage.launch("image/*")
                    }
                ) {
                    Text(text = "Pick image")
                }


            }
        }
    }
}
*/*/
