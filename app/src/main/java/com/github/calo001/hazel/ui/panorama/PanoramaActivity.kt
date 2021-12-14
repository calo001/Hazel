package com.github.calo001.hazel.ui.panorama

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.github.calo001.hazel.R
import android.widget.Toast

import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.platform.DataStoreProvider
import com.github.calo001.hazel.util.isZero
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.huawei.hms.panorama.PanoramaInterface

import com.huawei.hms.panorama.Panorama
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.collect


class PanoramaActivity : AppCompatActivity() {
    companion object {
        const val DRAWABLE_ID = "drawable_id"
        const val TITLE = "title"
        fun launch(context: Context, drawableId: Int, title: String) {
            val intent = Intent(context, PanoramaActivity::class.java)
            intent.putExtra(DRAWABLE_ID, drawableId)
            intent.putExtra(TITLE, title)
            context.startActivity(intent)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panorama)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        );

        val drawableId = intent.getIntExtra(DRAWABLE_ID, 0)
        val title = intent.getStringExtra(TITLE) ?: resources.getString(R.string.app_name)
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
                titleDescription.text = title
            }
        }

        val imageUri = (Uri.Builder())
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(drawableId))
            .appendPath(resources.getResourceTypeName(drawableId))
            .appendPath(resources.getResourceEntryName(drawableId))
            .build()

        val localInstance = Panorama.getInstance().getLocalInstance(this)
        if (localInstance.init().isZero and localInstance.setImage(
                imageUri,
                PanoramaInterface.IMAGE_TYPE_SPHERICAL
            ).isZero
        ) {
            val layout = findViewById<RelativeLayout>(R.id.relative_layout_panorama)
            val panoramaView: View = localInstance.view
            layout.addView(panoramaView)
            layout.setOnTouchListener { _, motionEvent ->
                localInstance.updateTouchEvent(motionEvent)
                true
            }

            lifecycle.addObserver(object: LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if(event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                        layout.removeView(panoramaView)
                        localInstance.deInit()
                    }
                }
            })
        } else {
            Logger.e( "local api error")
            Toast.makeText(this, "Local init error!", Toast.LENGTH_LONG).show()
        }
    }
}