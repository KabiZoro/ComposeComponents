@file:OptIn(FlowPreview::class)

package com.kabi.composecomponents.lazy

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlin.getValue
import androidx.core.content.edit
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@Composable
fun LazyList(modifier: Modifier = Modifier){
    val context = LocalContext.current
    val prefs by lazy {
        context.getSharedPreferences("prefs", MODE_PRIVATE)
    }
    val scrollPosition = prefs.getInt("scroll_position", 0)

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition
    )

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex
        }
            .debounce(500L)
            .collectLatest { index ->
            prefs.edit {
                putInt("scroll_position", index)
            }
        }
    }
    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(100){
            Text(
                text = "$it",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}