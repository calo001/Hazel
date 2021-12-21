package com.github.calo001.hazel.huawei

import android.content.IntentSender
import androidx.activity.ComponentActivity
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.orhanobut.logger.Logger

class LocationHelper(private val activity: ComponentActivity) {
    companion object {
        const val REQUEST_LOCATION_CODE = 9999
    }

    private var settingsClient: SettingsClient = LocationServices.getSettingsClient(activity)
    private lateinit var mLocationRequest: LocationRequest

    private var onSearchingLocation: (() -> Unit)? = null
    private var onLocationAvailable: (() -> Unit)? = null
    private var onFailureLocationAvailable: (() -> Unit)? = null

    private fun checkLocation(tryToGetPermissions: Boolean = true) {
            this.onSearchingLocation?.invoke()
            val builder = LocationSettingsRequest.Builder()
            mLocationRequest = LocationRequest()
            builder.addLocationRequest(mLocationRequest)
            val locationSettingsRequest = builder.build()
            // Check the device location settings.
            settingsClient?.checkLocationSettings(locationSettingsRequest)
                // Define the listener for success in calling the API for checking device location settings.
                ?.addOnSuccessListener { locationSettingsResponse ->
                    val locationSettingsStates = locationSettingsResponse.locationSettingsStates
                    val stringBuilder = StringBuilder()
                    // Checks whether the location function is enabled.
                    stringBuilder
                        .append("isLocationUsable=")
                        .append(locationSettingsStates.isLocationUsable)
                    // Checks whether HMS Core (APK) is available.
                    stringBuilder
                        .append(",\nisHMSLocationUsable=")
                        .append(locationSettingsStates.isHMSLocationUsable)
                    Logger.i("LocationHelper", "checkLocationSetting onComplete:$stringBuilder")
                    this.onLocationAvailable?.invoke()
                }
                // Define callback for failure in checking the device location settings.
                ?.addOnFailureListener { e ->
                    // Processing when the device is a Huawei device and has HMS Core (APK) installed, but its settings do not meet the location requirements.
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            if (tryToGetPermissions) {
                                // Call startResolutionForResult to display a popup message requesting the user to enable relevant permissions.
                                rae.startResolutionForResult(activity, REQUEST_LOCATION_CODE)
                                this.onSearchingLocation?.invoke()
                            } else {
                                this.onFailureLocationAvailable?.invoke()
                            }
                        } catch (sie: IntentSender.SendIntentException) {
                            this.onFailureLocationAvailable?.invoke()
                        }
                    }
                }
    }

    fun checkLocationSettings(
        onLocationAvailable: () -> Unit,
        onFailureLocationAvailable: () -> Unit,
        onSearchingLocation: () -> Unit,
        tryToGetPermissions: Boolean = true
    ) {
        this.onLocationAvailable = onLocationAvailable
        this.onFailureLocationAvailable = onFailureLocationAvailable
        this.onSearchingLocation = onSearchingLocation
        checkLocation(tryToGetPermissions)
    }
}