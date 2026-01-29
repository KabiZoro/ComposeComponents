package com.kabi.composecomponents

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.kabi.composecomponents.android_basics.broadcast_receiver.AirPlaneModeReceiver
import com.kabi.composecomponents.android_basics.broadcast_receiver.TestReceiver
import com.kabi.composecomponents.android_basics.intent.UriImageViewModel
import com.kabi.composecomponents.android_basics.work_manager.PhotoCompressionWorker
import com.kabi.composecomponents.barcode_scanner.BarcodeScannerScreen
import com.kabi.composecomponents.horizontal_pager.HorizontalPagerScreen
import com.kabi.composecomponents.ui.theme.ComposeComponentsTheme
import com.kabi.composecomponents.uri.ImageViewModel

// content providers -- https://youtu.be/IVHZpTyVOxU

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageViewModel>()

    private val imageViewModel by viewModels<UriImageViewModel>()

    private val airPlaneModeReceiver = AirPlaneModeReceiver()
    private val testReceiver = TestReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(
            airPlaneModeReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )
        registerReceiver(
            testReceiver,
            IntentFilter("TEST_RECEIVER")
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        /*ActivityCompat.requestPermissions(
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
        }*/

        enableEdgeToEdge()
        setContent {
            ComposeComponentsTheme {
                /*Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    imageViewModel.uri?.let {
                        AsyncImage(
                            model = imageViewModel.uri,
                            contentDescription = null
                        )
                    }
                    Button(
                        onClick = {
                            sendBroadcast(
                                Intent("TEST_ACTION")
                            )
                            *//*Intent(applicationContext, SecondActivity::class.java).also {
                                startActivity(it)
                            }*//*
                            *//*Intent(Intent.ACTION_MAIN).also {
                                it.`package` = "com.google.android.youtube"
                                try {
                                    startActivity(it)
                                } catch (e: ActivityNotFoundException) {
                                    e.printStackTrace()
                                }
                            }*//*
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_EMAIL, arrayOf("test@test.com"))
                                putExtra(Intent.EXTRA_SUBJECT, "This is my subject")
                                putExtra(Intent.EXTRA_TEXT, "This is the content")
                            }
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            }
                        }
                    ) { Text(text = "Click Here") }
                }*/

                BarcodeScannerScreen()
//                HorizontalPagerScreen()

                // foreground service
                /*Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            Intent(applicationContext, RunningService::class.java).also {
                                it.action = RunningService.Actions.START.toString()
                                startService(it)
                            }
                        }
                    ) {
                        Text("Start run")
                    }
                    Button(
                        onClick = {
                            Intent(applicationContext, RunningService::class.java).also {
                                it.action = RunningService.Actions.STOP.toString()
                                startService(it)
                            }
                        }
                    ) {
                        Text("Stop run")
                    }
                }*/
//                AudioMetadataScreen()
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }?: return

        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>()
            .setInputData(
                workDataOf(
                    PhotoCompressionWorker.KEY_CONTENT_URI to uri.toString(),
                )
            )

        // foreground service
        /*val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        imageViewModel.updateUri(uri)*/
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneModeReceiver)
        unregisterReceiver(testReceiver)
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
