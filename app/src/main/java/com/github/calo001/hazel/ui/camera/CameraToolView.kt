package com.github.calo001.hazel.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.calo001.hazel.R
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.huawei.TextRecognitionStatus
import com.github.calo001.hazel.ui.common.HazelToolbarSimple
import com.github.calo001.hazel.ui.theme.HazelTheme
import com.github.calo001.hazel.ui.theme.Lato
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import android.graphics.BitmapFactory

import android.content.ContentResolver
import androidx.compose.ui.platform.LocalContext
import com.github.calo001.hazel.huawei.BarcodeDetectorStatus


@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@Composable
fun CameraToolView(
    onBackClick: () -> Unit,
    onAnalysisCapture: (Bitmap, CameraFeature) -> Unit,
    onAnalysisStart: (CameraFeature) -> Unit,
    onAnalysisCaptureError: (CameraFeature) -> Unit,
    textRecognition: TextRecognitionStatus,
    barcodeStatus: BarcodeDetectorStatus,
) {
    // Track if the user doesn't want to see the rationale any more.
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    PermissionRequired(
        permissionState = cameraPermissionState,
        permissionNotGrantedContent = {
            if (doNotShowRationale) {
                RationaleMessage(
                    onBackClick = onBackClick,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                RequestCameraPermissionView(
                    onBackClick = onBackClick,
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    onDoNotShowRationale = { doNotShowRationale = true  },
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        permissionNotAvailableContent = {
            RationaleMessage(
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    ) {
        val context = LocalContext.current
        var featureSelected by rememberSaveable {
            mutableStateOf(CameraFeature.TextRecognizer.name)
        }
        CameraView(
            onImageCaptured = { uri, success, cameraFeature ->
                val contentResolver: ContentResolver = context.contentResolver
                contentResolver.openInputStream(uri).use { input ->
                    try {
                        val bitmap = BitmapFactory.decodeStream(input)
                        onAnalysisCapture(bitmap, cameraFeature)
                    } catch (e: Exception) {
                        onAnalysisCaptureError(CameraFeature.fromString(featureSelected))
                    }
                }
            },
            onError = {
                Logger.i(it.message ?: "error camera")
                onAnalysisCaptureError(CameraFeature.fromString(featureSelected))
            },
            textRecognition = textRecognition,
            barcodeStatus = barcodeStatus,
            onTakePhotoClick = { onAnalysisStart(CameraFeature.fromString(featureSelected)) },
            onNavBack = onBackClick,
            featureSelected = CameraFeature.fromString(featureSelected),
            onFeatureSelectedChange = { featureSelected = it.name }
        )
    }
}


@ExperimentalComposeUiApi
@Composable
fun RationaleMessage(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        HazelToolbarSimple(
            onBackClick = onBackClick,
            title = "Camera tool",
            subtitle = "Basic vocabulary",
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.openmoji_e103),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )
            Text(
                text = "Feature not available.",
                style = MaterialTheme.typography.h6.copy(
                    fontFamily = Lato
                ),
                modifier = Modifier.padding(16.dp)
            )
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(16.dp)) {
            Text(text = "Back to main")
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun RequestCameraPermissionView(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDoNotShowRationale: () -> Unit,
    onRequestPermission: () -> Unit,
) {
    ConstraintLayout(modifier) {
        val (toolbar, content, buttons) = createRefs()
        createVerticalChain(
            toolbar, content, buttons,
            chainStyle = ChainStyle.SpreadInside
        )

        HazelToolbarSimple(
            onBackClick = onBackClick,
            title = "Camera tool",
            subtitle = "Basic vocabulary",
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                }
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(content) {
                centerHorizontallyTo(parent)
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.openmoji_1f4f8),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )
            Text(
                text = "This tool requires to use of the camera for text recognition and QR reader features. Please grant the permission.",
                style = MaterialTheme.typography.h6.copy(
                    fontFamily = Lato
                ),
                modifier = Modifier.padding(16.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .constrainAs(buttons) {
                    centerHorizontallyTo(parent)
                }
        ) {
            Button(onClick = onRequestPermission) {
                Text(text = "Allow camera")
            }
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedButton(onClick = onDoNotShowRationale) {
                Text(
                    text = "Deny camera",
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // CameraX Preview UseCase
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()
                val imageCapture = ImageCapture.Builder().build()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase, imageCapture
                    )
                } catch (ex: Exception) {
                    Logger.e("CameraPreview", "Use case binding failed", ex)
                }
            }

            previewView
        }
    )
}

//suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
//    ProcessCameraProvider.getInstance(this).also { future ->
//        future.addListener({
//            continuation.resume(future.get())
//        }, executor)
//    }
//}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@androidx.compose.ui.tooling.preview.Preview(showSystemUi = true)
@Composable
fun RequestCameraPermissionViewPreview() {
    HazelTheme(colorVariant = ColorVariant.Green) {
        RequestCameraPermissionView(
            modifier = Modifier.fillMaxSize(),
            onBackClick = {},
            onDoNotShowRationale = {},
            onRequestPermission = {},
        )
    }
}