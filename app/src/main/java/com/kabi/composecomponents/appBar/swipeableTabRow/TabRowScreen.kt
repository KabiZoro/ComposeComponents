package com.kabi.composecomponents.appBar.swipeableTabRow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kabi.composecomponents.ui.theme.CodingGreen
import com.kabi.composecomponents.ui.theme.CodingLightGrey

@Composable
fun TabRowScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        var selectedTabIndex by remember {
            mutableIntStateOf(0)
        }
        val pagerState = rememberPagerState {
            tabItems.size
        }
        LaunchedEffect(selectedTabIndex) {
            // when selected tab changes the pager scroll to it
            pagerState.animateScrollToPage(selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            // when the current page or the scroll ends then the tab changes
            if (!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = CodingLightGrey,
                contentColor = CodingGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[selectedTabIndex]
                        ),
                        color = CodingGreen
                    )
                }
            ) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(text = item.title)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedTabIndex) {
                                    item.selectedItem
                                } else item.unselectedItem,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CodingLightGrey),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabItems[index].title,
                        color = CodingGreen
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TabRowScreenPreview() {
    TabRowScreen()
}