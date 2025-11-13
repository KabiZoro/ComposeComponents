package com.kabi.composecomponents.navBar.navRail

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationRailItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)
