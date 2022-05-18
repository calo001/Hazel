package com.github.calo001.hazel.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.calo001.hazel.R
import com.github.calo001.hazel.providers.AppLinkingHelper
import com.github.calo001.hazel.providers.getAppLinking
import com.github.calo001.hazel.platform.QRGenerator
import com.github.calo001.hazel.ui.common.HazelToolbarButton
import com.github.calo001.hazel.ui.main.DialogShareQRStatus
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShareDialog(
    dialogShareQRStatus: DialogShareQRStatus,
    onDismissRequest: () -> Unit,
    updateDialogShareQRStatus: (DialogShareQRStatus) -> Unit,
    onShareUrl: (String) -> Unit,
) {
    if (dialogShareQRStatus is DialogShareQRStatus.RawRoute ||
        dialogShareQRStatus is DialogShareQRStatus.AppLinkingLink
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
        ) {
            when (dialogShareQRStatus) {
                DialogShareQRStatus.Normal -> {}
                is DialogShareQRStatus.RawRoute -> {
                    val route = (dialogShareQRStatus as? DialogShareQRStatus.RawRoute)?.route ?: ""
                    val urlRoute = "https://calo001.github.io/hazel-web/$route"
                    LaunchedEffect(key1 = route) {
                        getAppLinking(
                            title = "Share",
                            route = urlRoute,
                            appLinkingHelper = AppLinkingHelper(),
                            onSuccess = { linkHuawei ->
                                updateDialogShareQRStatus(DialogShareQRStatus.AppLinkingLink(linkHuawei, urlRoute))
                            },
                            onError = { onDismissRequest() },
                        )
                    }
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.9f),
                        elevation = 16.dp,
                        modifier = Modifier
                            .width(300.dp)
                            .height(390.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.loading_drop)
                            )
                            LottieAnimation(
                                composition = composition,
                                contentScale = ContentScale.FillHeight,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier
                                    .size(80.dp)
                            )
                        }
                    }
                }
                is DialogShareQRStatus.AppLinkingLink -> {
                    val link = (dialogShareQRStatus as? DialogShareQRStatus.AppLinkingLink)?.link ?: ""
                    val routeLink = (dialogShareQRStatus as? DialogShareQRStatus.AppLinkingLink)?.route ?: ""
                    if (link.isNotEmpty()) {
                        val qrGenerator = QRGenerator(routeLink).getBarcodeBitmapSync()
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.9f),
                            elevation = 16.dp,
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Image(
                                    bitmap = qrGenerator.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.size(300.dp)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .fillMaxWidth()
                                ) {
                                    Button(onClick = { onShareUrl(link) }) {
                                        Text(
                                            text = "Share link",
                                            style = MaterialTheme.typography.h6
                                        )
                                    }
                                    HazelToolbarButton(
                                        icon = Icons.Filled.Close,
                                        onClick = {
                                            updateDialogShareQRStatus(
                                                DialogShareQRStatus.Normal
                                            )
                                        },
                                        modifier = Modifier
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                //SimpleRoundedBanner()
                            }
                        }
                    }
                }
            }
        }
    }
}