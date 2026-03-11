package com.kabi.composecomponents.deepLink

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object List : Route
    @Serializable
    data class Detail(val id: Int) : Route
}