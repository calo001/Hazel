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
import com.github.calo001.hazel.databinding.ActivityMapBinding
import com.github.calo001.hazel.model.hazeldb.Country
import com.github.calo001.hazel.platform.DataStoreProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val COUNTRY_NAME = "country_name"
        const val COUNTRY_LATITUDE = "country_latitude"
        const val COUNTRY_LONGITUDE = "country_longitude"
        const val COUNTRY_ZOOM = "country_zoom"

        fun launch(context: Context, country: Country) {
            val intent = Intent(context, MapActivity::class.java).apply {
                putExtra(COUNTRY_NAME, country.name)
                putExtra(COUNTRY_LATITUDE, country.latitude)
                putExtra(COUNTRY_LONGITUDE, country.longitude)
                putExtra(COUNTRY_ZOOM, country.zoom)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val countryLatitude = intent.getStringExtra(COUNTRY_LATITUDE)?.toDoubleOrNull() ?: 0.0
        val countryLongitude = intent.getStringExtra(COUNTRY_LONGITUDE)?.toDoubleOrNull() ?: 0.0
        val countryZoom = intent.getIntExtra(COUNTRY_ZOOM, 0)
        val country = LatLng(countryLatitude, countryLongitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(country, countryZoom.toFloat()))
    }
}