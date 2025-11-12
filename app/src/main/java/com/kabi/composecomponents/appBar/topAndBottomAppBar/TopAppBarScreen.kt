@file:OptIn(ExperimentalMaterial3Api::class)

package com.kabi.composecomponents.appBar.topAndBottomAppBar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kabi.composecomponents.ui.theme.CodingGreen
import com.kabi.composecomponents.ui.theme.CodingGrey
import com.kabi.composecomponents.ui.theme.CodingLightGrey

@Composable
fun TopAppBarScreen() {
    val scrollBehavior = TopAppBarDefaults
//        .enterAlwaysScrollBehavior()
//        .exitUntilCollapsedScrollBehavior()
        .pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = CodingLightGrey,
        contentColor = CodingGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Top App Bar")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CodingLightGrey,
                    navigationIconContentColor = CodingGreen,
                    titleContentColor = CodingGreen,
                    actionIconContentColor = CodingGreen,
                    scrolledContainerColor = CodingGrey
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(100) { index ->
                Text(
                    text = "Item $index",
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TopAppBarScreenPreview() {
    TopAppBarScreen()
}