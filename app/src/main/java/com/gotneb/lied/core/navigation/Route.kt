package com.gotneb.lied.core.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object MusicList : Route()

    @Serializable
    data object MusicPlayer : Route()
}