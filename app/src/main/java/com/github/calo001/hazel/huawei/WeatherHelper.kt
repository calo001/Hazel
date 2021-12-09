package com.github.calo001.hazel.huawei

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.annotation.RequiresPermission
import com.github.calo001.hazel.R
import com.github.calo001.hazel.util.checkPermission
import com.huawei.hms.kit.awareness.Awareness
import com.huawei.hms.kit.awareness.status.weather.constant.WeatherId

class WeatherHelper(
    private val activity: Activity,
    private val result: (WeatherStatus) -> Unit
) {
    fun checkWeatherFromResult(requestCode: Int) {
        if (requestCode == LocationHelper.REQUEST_LOCATION_CODE) {
            if (activity.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) and
                activity.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                initWhetherListener()
            } else {
                result(WeatherStatus.LocationNotGranted)
            }
        }
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun initWhetherListener() {
        Awareness.getCaptureClient(activity).currentLocation.addOnSuccessListener {
        Awareness.getCaptureClient(activity).weatherByDevice
            // Callback listener for execution success.
            .addOnSuccessListener { weatherStatusResponse ->
                val weatherStatus = weatherStatusResponse.weatherStatus
                val weatherSituation = weatherStatus.weatherSituation
                val situation = weatherSituation.situation
                // For more weather information, please refer to the API Reference of Awareness Kit.
                val weatherInfoStr = """
                  City:${weatherSituation?.city?.name}                
                  Weather id is ${situation?.weatherId}                
                  CN Weather id is ${situation?.cnWeatherId}                
                  Temperature is ${situation?.temperatureC}℃,${situation?.temperatureF}℉     
                  Wind speed is ${situation?.windSpeed}km/h                
                  Wind direction is ${situation?.windDir}       
                  Humidity is ${situation?.humidity}%                
                  """.trimIndent()
                Log.i("WeatherHelper", weatherInfoStr)
                if (situation != null) {
                    result(WeatherStatus.Success(
                        temperature = situation.temperatureC.toInt().toString(),
                        typeWeather = WeatherType.getFromInt(situation.weatherId)
                    ))
                } else {
                    result(WeatherStatus.Error)
                }
            }
            // Callback listener for execution failure.
            .addOnFailureListener { e: Exception? ->
                Log.d("WeatherHelper", "get weather capture failed ${e?.message}")
                result(WeatherStatus.Error)
            }
        }.addOnFailureListener {
            result(WeatherStatus.LocationFailure)
        }

    }
}

sealed interface WeatherStatus {
    object Loading: WeatherStatus
    class  Success(val temperature: String, val typeWeather: WeatherType): WeatherStatus
    object LocationNotGranted: WeatherStatus
    object LocationFailure: WeatherStatus
    object Error: WeatherStatus
}

sealed class WeatherType {
    object Sunny:                       WeatherType()
    object MostlySunny:                 WeatherType()
    object PartlySunny:                 WeatherType()
    object IntermittentClouds:          WeatherType()
    object HazySunshine:                WeatherType()
    object MostlyCloudy:                WeatherType()
    object Cloudy:                      WeatherType()
    object Dreary:                      WeatherType()
    object Fog:                         WeatherType()
    object Showers:                     WeatherType()
    object MostlyCloudyWithShowers:     WeatherType()
    object PartlySunnyWithShowers:      WeatherType()
    object TStorms:                     WeatherType()
    object MostlyCloudyWithTStorm:      WeatherType()
    object PartlySunnyWithTStorm:       WeatherType()
    object Rain:                        WeatherType()
    object Flurries:                    WeatherType()
    object MostlyCloudyWithFlurries:    WeatherType()
    object PartlySunnyWithFlurries:     WeatherType()
    object Snow:                        WeatherType()
    object MostlyCloudyWithSnow:        WeatherType()
    object Ice:                         WeatherType()
    object Sleet:                       WeatherType()
    object FreezingRain:                WeatherType()
    object RainAndSnow:                 WeatherType()
    object Hot:                         WeatherType()
    object Cold:                        WeatherType()
    object Windy:                       WeatherType()
    object Clear:                       WeatherType()
    object MostlyClear:                 WeatherType()
    object PartlyCloudy:                WeatherType()
    object IntermittentClouds2:         WeatherType()
    object HazyMoonLight:               WeatherType()
    object MostlyCloudy2:               WeatherType()
    object PartlyCloudyWithShowers:     WeatherType()
    object MostlyCloudyWithShowers2:    WeatherType()
    object PartlyCloudyWithTStorms:     WeatherType()
    object MostlyCloudyWithTStorms2:    WeatherType()
    object MostlyCloudyWithFlurries2:   WeatherType()
    object MostlyCloudyWithSnow2:       WeatherType()
    object Unknown:                     WeatherType()

    companion object {
        fun getIdRes(weatherType: WeatherType): List<Int> = when(weatherType) {
            Sunny -> listOf(R.drawable.openmoji_2600)
            MostlySunny -> listOf(R.drawable.openmoji_1f324)
            PartlySunny -> listOf(R.drawable.openmoji_26c5)
            IntermittentClouds -> listOf(R.drawable.openmoji_1f32b)
            HazySunshine -> listOf(R.drawable.openmoji_1f636_200d_1f32b_fe0f)
            MostlyCloudy -> listOf(R.drawable.openmoji_1f325)
            Cloudy -> listOf(R.drawable.openmoji_2601)
            Dreary -> listOf(R.drawable.openmoji_e20e)
            Fog -> listOf(R.drawable.openmoji_1f301)
            Showers -> listOf(R.drawable.openmoji_2614)
            MostlyCloudyWithShowers -> listOf(R.drawable.openmoji_1f327)
            PartlySunnyWithShowers -> listOf(R.drawable.openmoji_1f326)
            TStorms -> listOf(R.drawable.openmoji_26a1)
            MostlyCloudyWithTStorm -> listOf(R.drawable.openmoji_1f329)
            PartlySunnyWithTStorm -> listOf(R.drawable.openmoji_2600, R.drawable.openmoji_1f329)
            Rain -> listOf(R.drawable.openmoji_2614)
            Flurries -> listOf(R.drawable.openmoji_2744)
            MostlyCloudyWithFlurries -> listOf(R.drawable.openmoji_1f328)
            PartlySunnyWithFlurries -> listOf(R.drawable.openmoji_2600, R.drawable.openmoji_1f328)
            Snow -> listOf(R.drawable.openmoji_2744)
            MostlyCloudyWithSnow -> listOf(R.drawable.openmoji_1f328)
            Ice -> listOf(R.drawable.openmoji_1f9ca)
            Sleet -> listOf(R.drawable.openmoji_1f4a6)
            FreezingRain -> listOf(R.drawable.openmoji_1f976)
            RainAndSnow -> listOf(R.drawable.openmoji_1f328)
            Hot -> listOf(R.drawable.openmoji_1f975)
            Cold -> listOf(R.drawable.openmoji_e2c0)
            Windy -> listOf(R.drawable.openmoji_1f32c)
            Clear -> listOf(R.drawable.openmoji_1f3f3_fe0f_200d_1f7e6, R.drawable.openmoji_2600)
            MostlyClear -> listOf(R.drawable.openmoji_1f3f3_fe0f_200d_1f7e6, R.drawable.openmoji_1f324)
            PartlyCloudy -> listOf(R.drawable.openmoji_26c5)
            IntermittentClouds2 -> listOf(R.drawable.openmoji_1f32b)
            HazyMoonLight -> listOf(R.drawable.openmoji_1f391)
            MostlyCloudy2 -> listOf(R.drawable.openmoji_1f325)
            PartlyCloudyWithShowers -> listOf(R.drawable.openmoji_1f3f3_fe0f_200d_1f7e6, R.drawable.openmoji_1f328)
            MostlyCloudyWithShowers2 -> listOf(R.drawable.openmoji_1f3f3_fe0f_200d_1f7e6, R.drawable.openmoji_1f329)
            PartlyCloudyWithTStorms -> listOf(R.drawable.openmoji_1f329)
            MostlyCloudyWithTStorms2 -> listOf(R.drawable.openmoji_1f329)
            MostlyCloudyWithFlurries2 -> listOf(R.drawable.openmoji_1f328)
            MostlyCloudyWithSnow2 -> listOf(R.drawable.openmoji_2603)
            Unknown -> listOf(R.drawable.openmoji_1f325)
        }

        fun getFromInt(id: Int) =
            when(id) {
                WeatherId.SUNNY -> Sunny
                WeatherId.MOSTLY_SUNNY -> MostlySunny
                WeatherId.PARTLY_SUNNY -> PartlySunny
                WeatherId.INTERMITTENT_CLOUDS -> IntermittentClouds
                WeatherId.HAZY_SUNSHINE -> HazySunshine
                WeatherId.MOSTLY_CLOUDY -> MostlyCloudy
                WeatherId.CLOUDY -> Cloudy
                WeatherId.DREARY -> Dreary
                WeatherId.FOG ->  Fog
                WeatherId.SHOWERS ->  Showers
                WeatherId.MOSTLY_CLOUDY_WITH_SHOWERS -> MostlyCloudyWithShowers
                WeatherId.PARTLY_SUNNY_WITH_SHOWERS -> PartlySunnyWithShowers
                WeatherId.T_STORMS -> TStorms
                WeatherId.MOSTLY_CLOUDY_WITH_T_STORMS -> MostlyCloudyWithTStorm
                WeatherId.PARTLY_SUNNY_WITH_T_STORMS -> PartlySunnyWithTStorm
                WeatherId.RAIN -> Rain
                WeatherId.FLURRIES -> Flurries
                WeatherId.MOSTLY_CLOUDY_WITH_FLURRIES -> MostlyCloudyWithFlurries
                WeatherId.PARTLY_SUNNY_WITH_FLURRIES -> PartlySunnyWithFlurries
                WeatherId.SNOW -> Snow
                WeatherId.MOSTLY_CLOUDY_WITH_SNOW -> MostlyCloudyWithSnow
                WeatherId.ICE -> Ice
                WeatherId.SLEET -> Sleet
                WeatherId.FREEZING_RAIN -> FreezingRain
                WeatherId.RAIN_AND_SNOW -> RainAndSnow
                WeatherId.HOT -> Hot
                WeatherId.COLD -> Cold
                WeatherId.WINDY -> Windy
                WeatherId.CLEAR -> Clear
                WeatherId.MOSTLY_CLEAR -> MostlyClear
                WeatherId.PARTLY_CLOUDY -> PartlyCloudy
                WeatherId.INTERMITTENT_CLOUDS_2 -> IntermittentClouds2
                WeatherId.HAZY_MOONLIGHT -> HazyMoonLight
                WeatherId.MOSTLY_CLOUDY_2 -> MostlyCloudy2
                WeatherId.PARTLY_CLOUDY_WITH_SHOWERS -> PartlyCloudyWithShowers
                WeatherId.MOSTLY_CLOUDY_WITH_SHOWERS_2 -> MostlyCloudyWithShowers2
                WeatherId.PARTLY_CLOUDY_WITH_T_STORMS -> PartlyCloudyWithTStorms
                WeatherId.MOSTLY_CLOUDY_WITH_T_STORMS_2 -> MostlyCloudyWithTStorms2
                WeatherId.MOSTLY_CLOUDY_WITH_FLURRIES_2 -> MostlyCloudyWithFlurries2
                WeatherId.MOSTLY_CLOUDY_WITH_SNOW_2 -> MostlyCloudyWithSnow2
                else -> Unknown
            }
    }
}


