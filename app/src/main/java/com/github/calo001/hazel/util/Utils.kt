package com.github.calo001.hazel.util

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.github.calo001.hazel.R

class PainterIdentifier(
    private val resources: Resources,
    private val packageName: String,
    private val default: Int,
    ) {
    @Composable
    fun getPainter(identifier: String): Painter {
        val resourceId = resources.getIdentifier(
            identifier, "drawable",
            packageName
        )

        return if (resourceId != 0) {
            painterResource(id = resourceId)
        } else {
            painterResource(id = default)
        }
    }
}