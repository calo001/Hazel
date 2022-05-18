package com.github.calo001.hazel.ui.panorama

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.platform.DataStoreProvider
import com.github.calo001.hazel.providers.PanoramaHelper
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.orhanobut.logger.Logger

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

    private fun hideSystemBars() {
//        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
//        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Tell the window that we want to handle/fit any system windows
            WindowCompat.setDecorFitsSystemWindows(window, false)

            val controller = window.decorView.windowInsetsController

            // Hide the keyboard (IME)
            controller?.hide(WindowInsets.Type.ime())

            // Sticky Immersive is now ...
            controller?.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // When we want to hide the system bars
            controller?.hide(WindowInsets.Type.systemBars())

            /*val flag = WindowInsets.Type.statusBars()
            WindowInsets.Type.navigationBars()
            WindowInsets.Type.captionBar()
            window?.insetsController?.hide(flag)*/
        } else {
            //noinspection
            @Suppress("DEPRECATION")
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        hideSystemBars()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panorama)

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

        val localInstance = PanoramaHelper(this)
        if (localInstance.validate(imageUri)) {
            val layout = findViewById<RelativeLayout>(R.id.relative_layout_panorama)
            val panoramaView: View = localInstance.getView(this)
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