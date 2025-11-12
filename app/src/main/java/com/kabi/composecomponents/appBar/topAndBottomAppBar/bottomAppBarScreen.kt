package com.kabi.composecomponents.appBar.topAndBottomAppBar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kabi.composecomponents.ui.theme.CodingGreen
import com.kabi.composecomponents.ui.theme.CodingGrey
import com.kabi.composecomponents.ui.theme.CodingLightGrey

@Composable
fun bottomAppBarScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = CodingLightGrey,
        contentColor = CodingGreen,
        bottomBar = {
            BottomAppBar(
                containerColor = CodingGrey,
                contentColor = CodingGreen,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = CodingGreen,
                        contentColor = CodingGrey
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        /*floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null
                    )
                }
            }
        }*/
    ) { paddingValues ->

    }
}

@Preview
@Composable
private fun bottomAppBarScreenPreview() {
    bottomAppBarScreen()
}