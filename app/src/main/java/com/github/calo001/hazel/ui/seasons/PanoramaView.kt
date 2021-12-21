package com.github.calo001.hazel.ui.seasons

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.github.calo001.hazel.util.isZero
import com.github.calo001.hazel.util.setAllMatchParent
import com.huawei.hms.panorama.PanoramaInterface
import com.orhanobut.logger.Logger

@SuppressLint("ClickableViewAccessibility")
@Composable
fun PanoramaView(
    modifier: Modifier = Modifier,
    drawableId: Int,
    panorama: PanoramaInterface.PanoramaLocalInterface,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    var drawable by remember { mutableStateOf(drawableId) }
    drawable = drawableId
    AndroidView(
        modifier = modifier,
        factory = { context ->
            RelativeLayout(context).apply {
                setAllMatchParent()
                    lifecycleOwner.lifecycle.addObserver(object: LifecycleEventObserver {
                        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                            if (event == Lifecycle.Event.ON_RESUME) {
                                if (this@apply.childCount == 0) {
                                    val imageUri = (Uri.Builder())
                                        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                                        .authority(context.resources.getResourcePackageName(drawable))
                                        .appendPath(context.resources.getResourceTypeName(drawable))
                                        .appendPath(context.resources.getResourceEntryName(drawable))
                                        .build()

                                    if (panorama.init().isZero and panorama.setImage(
                                            imageUri,
                                            PanoramaInterface.IMAGE_TYPE_SPHERICAL
                                        ).isZero
                                    ) {
                                        runCatching {
                                            (panorama.view.parent as ViewGroup).removeAllViews()
                                        }
                                        try {
                                            val panoramaView = panorama.view
                                            this@apply.addView(panoramaView)
                                            this@apply.setOnTouchListener { _, motionEvent ->
                                                panorama.updateTouchEvent(motionEvent)
                                                true
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    } else {
                                        Logger.e("local api error")
                                    }
                                }
                            }

                            if(event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                                (panorama.view.parent as? ViewGroup)?.removeAllViews()
                            }
                    }
                })
            }
        },
        update = { relativeLayout ->
            val imageUri = (Uri.Builder())
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(relativeLayout.context.resources.getResourcePackageName(drawable))
                .appendPath(relativeLayout.context.resources.getResourceTypeName(drawable))
                .appendPath(relativeLayout.context.resources.getResourceEntryName(drawable))
                .build()

            runCatching {
                panorama.init()
                panorama.setImage(
                    imageUri,
                    PanoramaInterface.IMAGE_TYPE_SPHERICAL
                )
            }
        }
    )
}