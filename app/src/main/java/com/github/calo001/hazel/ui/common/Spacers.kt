package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun LazyListScope.safeSpacer(extraSpace: Dp = 0.dp) {
    item {
        Spacer(modifier = Modifier.height(140.dp + extraSpace))
    }
}