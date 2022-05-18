package com.github.calo001.hazel.providers

import android.app.Activity
import androidx.annotation.RequiresPermission
import com.github.calo001.hazel.R
import com.github.calo001.hazel.model.status.WeatherStatus

class WeatherHelper(
    private val activity: Activity,
    private val result: (WeatherStatus) -> Unit
) {
    init {
        result(WeatherStatus.NoAvailable)
    }
    fun checkWeatherFromResult(requestCode: Int) {
        result(WeatherStatus.NoAvailable)
    }

    fun initWhetherListener() {
        result(WeatherStatus.NoAvailable)
    }
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

        fun getFromInt(id: Int) = Unknown
    }
}