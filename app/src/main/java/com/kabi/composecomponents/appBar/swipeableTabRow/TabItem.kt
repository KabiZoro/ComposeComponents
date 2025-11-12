package com.kabi.composecomponents.appBar.swipeableTabRow

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(
    val title: String,
    val unselectedItem: ImageVector,
    val selectedItem: ImageVector
)

val tabItems = listOf(
    TabItem(
        title = "Home",
        unselectedItem = Icons.Outlined.Home,
        selectedItem = Icons.Filled.Home
    ),
    TabItem(
        title = "Browse",
        unselectedItem = Icons.Outlined.ShoppingCart,
        selectedItem = Icons.Filled.ShoppingCart
    ),
    TabItem(
        title = "Account",
        unselectedItem = Icons.Outlined.AccountCircle,
        selectedItem = Icons.Filled.AccountCircle
    )
)
