package com.github.calo001.hazel.config

sealed class DarkMode(val name: String) {
    object FollowSystem: DarkMode("follow_system")
    object Light: DarkMode("light")
    object Dark: DarkMode("dark")
}