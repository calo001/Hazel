package com.github.calo001.hazel.ui.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.calo001.hazel.R
import com.github.calo001.hazel.huawei.BarcodeDetectorStatus
import com.github.calo001.hazel.huawei.TextRecognitionStatus

@Composable
fun CameraControls(
    cameraUIAction: (CameraUIAction) -> Unit,
    textRecognitionStatus: TextRecognitionStatus,
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.primary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExtendedFloatingActionButton(
            text = {
                when (textRecognitionStatus) {
                    TextRecognitionStatus.Error -> Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = "error",
                        Modifier.padding(horizontal = 16.dp)
                    )
                    TextRecognitionStatus.Normal -> Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = "normal",
                        Modifier.padding(horizontal = 16.dp)
                    )
                    TextRecognitionStatus.Processing -> {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.loading_drop)
                        )
                        LottieAnimation(
                            composition = composition,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .height(48.dp)
                                .width(56.dp)
                        )
                    }
                    is TextRecognitionStatus.Result -> Icon(
                            imageVector = Icons.Filled.Camera,
                            contentDescription = "result",
                            Modifier.padding(horizontal = 16.dp)
                        )
                }
            },
            onClick = {
                if(textRecognitionStatus !is TextRecognitionStatus.Processing) {
                    cameraUIAction(CameraUIAction.OnCameraClick)
                }
            }
        )
    }
}

@Composable
fun CameraControls(
    cameraUIAction: (CameraUIAction) -> Unit,
    barcodeStatus: BarcodeDetectorStatus,
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.primary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExtendedFloatingActionButton(
            text = {
                when (barcodeStatus) {
                    BarcodeDetectorStatus.Error -> Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = "error",
                        Modifier.padding(horizontal = 16.dp)
                    )
                    BarcodeDetectorStatus.Normal -> Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = "normal",
                        Modifier.padding(horizontal = 16.dp)
                    )
                    BarcodeDetectorStatus.Processing -> {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.loading_drop)
                        )
                        LottieAnimation(
                            composition = composition,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .height(48.dp)
                                .width(56.dp)
                        )
                    }
                    is BarcodeDetectorStatus.Result -> Icon(
                            imageVector = Icons.Filled.Camera,
                            contentDescription = "result",
                            Modifier.padding(horizontal = 16.dp)
                    )
                }
            },
            onClick = {
                if(barcodeStatus !is BarcodeDetectorStatus.Processing) {
                    cameraUIAction(CameraUIAction.OnCameraClick)
                }
            }
        )
    }
}