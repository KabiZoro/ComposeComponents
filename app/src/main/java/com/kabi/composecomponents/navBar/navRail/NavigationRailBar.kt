@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.kabi.composecomponents.navBar.navRail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.kabi.composecomponents.ui.theme.CodingGreen
import com.kabi.composecomponents.ui.theme.CodingGrey
import com.kabi.composecomponents.ui.theme.CodingLightGrey

@Composable
fun NavigationRailBar(
    windowSizeClass: WindowSizeClass
) {
    val items = listOf(
        NavigationRailItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false
        ),
        NavigationRailItem(
            title = "Chat",
            selectedIcon = Icons.AutoMirrored.Filled.Chat,
            unselectedIcon = Icons.AutoMirrored.Outlined.Chat,
            hasNews = false,
            badgeCount = 45
        ),
        NavigationRailItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            hasNews = true
        )
    )
    val showNavigationRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    var selectedItemIndex by remember {
        mutableStateOf(0)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            if (!showNavigationRail) {
                // NavigationBar()
            }
        },
        containerColor = CodingLightGrey
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    start = if (showNavigationRail) 80.dp else 0.dp
                )
        ) {
            items(100) { index ->
                Text(
                    text = "Item $index",
                    color = CodingGreen,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
    if (showNavigationRail) {
        NavigationRailSideBar(
            items = items,
            selectedItemIndex = selectedItemIndex,
            onNavigate = { newIndex ->
                selectedItemIndex = newIndex
            }
        )
    }
}

@Composable
fun NavigationRailSideBar(
    items: List<NavigationRailItem>,
    selectedItemIndex: Int,
    onNavigate: (Int) -> Unit
) {
    NavigationRail(
        header = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
            FloatingActionButton(
                onClick = {},
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                contentColor = CodingGrey,
                containerColor = CodingGreen,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        containerColor = CodingGrey,
        contentColor = CodingGreen
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom)
        ) {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    selected = selectedItemIndex == index,
                    onClick = { onNavigate(index) },
                    icon = {
                        NavigationRailIcon(
                            item = item,
                            selected = selectedItemIndex == index
                        )
                    },
                    label = {
                        Text(text = item.title)
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = CodingGrey,
                        unselectedIconColor = CodingGreen,
                        selectedTextColor = CodingGreen,
                        unselectedTextColor = CodingGreen,
                        indicatorColor = CodingGreen
                    )
                )
            }
        }
    }
}

@Composable
fun NavigationRailIcon(
    item: NavigationRailItem,
    selected: Boolean
) {
    BadgedBox(
        badge = {
            if (item.badgeCount != null) {
                Badge {
                    Text(text = item.badgeCount.toString())
                }
            } else if (item.hasNews) {
                Badge()
            }
        }
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title
        )
    }
}

@Preview(device = TABLET)
@Composable
private fun NavigationRailBarPreview() {
    val windowSizeClass = WindowSizeClass
        .calculateFromSize(
            DpSize(673.dp, 841.dp)
        )
    NavigationRailBar(windowSizeClass)
}