package com.github.calo001.hazel.ui.map

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.platform.DataStoreProvider
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.LatLng
import kotlinx.coroutines.flow.collect

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val COUNTRY_NAME = "country_name"
        const val COUNTRY_LATITUDE = "country_latitude"
        const val COUNTRY_LONGITUDE = "country_longitude"

        fun launch(context: Context, country: Country) {
            val intent = Intent(context, MapActivity::class.java).apply {
                putExtra(COUNTRY_NAME, country.name)
                putExtra(COUNTRY_LATITUDE, country.latitude)
                putExtra(COUNTRY_LONGITUDE, country.longitude)
            }
            context.startActivity(intent)
        }
    }

    private var hMap: HuaweiMap? = null
    private var mSupportMapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val countryName = intent.getStringExtra(COUNTRY_NAME)
        val backButton = findViewById<FloatingActionButton>(R.id.fabBack)
        val titleDescription = findViewById<ExtendedFloatingActionButton>(R.id.fabTitle)

        backButton.setOnClickListener {
            onBackPressed()
        }

        val dataStore = DataStoreProvider(applicationContext)
        lifecycleScope.launchWhenCreated {
            backButton.show()
            titleDescription.show()
            dataStore.colorScheme.collect { colorVariant ->
                val colorHex = when (colorVariant) {
                    ColorVariant.Amber -> "#FFECB3"
                    ColorVariant.Blue -> "#82B1FF"
                    ColorVariant.Green -> "#B9F6CA"
                    ColorVariant.Indigo -> "#C5CAE9"
                    ColorVariant.Pink -> "#F8BBD0"
                    ColorVariant.Purple -> "#E1BEE7"
                }
                backButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorHex))
                titleDescription.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorHex))
                titleDescription.text = countryName
            }

        }

        mSupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapfragment) as SupportMapFragment?
        mSupportMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(huaweiMap: HuaweiMap?) {
        hMap = huaweiMap

        val countryLatitude = intent.getStringExtra(COUNTRY_LATITUDE)?.toDoubleOrNull() ?: 0.0
        val countryLongitude = intent.getStringExtra(COUNTRY_LONGITUDE)?.toDoubleOrNull() ?: 0.0

        hMap?.setOnMapLoadedCallback {
            val currentCountry = LatLng(countryLatitude, countryLongitude)
            hMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentCountry, 0f))

//            val currentCountry = LatLngBounds(
//                LatLng(countryLatitude, countryLongitude), LatLng(countryLatitude, countryLongitude)
//            )
//
//            hMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(currentCountry, 12))
        }
    }
}