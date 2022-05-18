package com.github.calo001.hazel.providers

import androidx.activity.ComponentActivity

class LocationHelper(private val activity: ComponentActivity) {
    companion object {
        const val REQUEST_LOCATION_CODE = 9999
    }

    private fun checkLocation(tryToGetPermissions: Boolean = true) {
    }

    fun checkLocationSettings(
        onLocationAvailable: () -> Unit,
        onFailureLocationAvailable: () -> Unit,
        onSearchingLocation: () -> Unit,
        tryToGetPermissions: Boolean = true
    ) {
    }
}