package com.github.calo001.hazel.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.calo001.hazel.config.ColorVariant
import com.github.calo001.hazel.model.status.BarcodeDetectorStatus
import com.github.calo001.hazel.model.status.TextRecognitionStatus
import com.github.calo001.hazel.ui.common.HazelToolbarCamera
import com.github.calo001.hazel.ui.common.SimpleChip
import com.github.calo001.hazel.ui.theme.HazelTheme
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraView(
    onImageCaptured: (Uri, Boolean, CameraFeature) -> Unit,
    onTakePhotoClick: () -> Unit,
    onError: (ImageCaptureException) -> Unit,
    textRecognition: TextRecognitionStatus,
    onNavBack: () -> Unit,
    featureSelected: CameraFeature,
    onFeatureSelectedChange: (CameraFeature) -> Unit,
    barcodeStatus: BarcodeDetectorStatus,
) {
    val context = LocalContext.current
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onImageCaptured(uri, true, featureSelected)
    }

    CameraPreviewView(
        textRecognition = textRecognition,
        barcodeStatus = barcodeStatus,
        imageCapture = imageCapture,
        lensFacing = lensFacing,
        onNavBack = onNavBack,
        featureSelected = featureSelected,
        onSelectCameraFeature = { onFeatureSelectedChange(it) },
        cameraUIAction = { cameraUIAction ->
            when (cameraUIAction) {
                is CameraUIAction.OnCameraClick -> {
                    onTakePhotoClick()
                    imageCapture.takePicture(
                        context = context,
                        lensFacing = lensFacing,
                        onImageCaptured = onImageCaptured,
                        featureSelected = featureSelected,
                        onError = onError,
                    )
                }
                is CameraUIAction.OnSwitchCameraClick -> {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                        else
                            CameraSelector.LENS_FACING_BACK
                }
                is CameraUIAction.OnGalleryViewClick -> {
                    if (true == context.getOutputDirectory().listFiles()?.isNotEmpty()) {
                        galleryLauncher.launch("image/*")
                    }
                }
            }
        }
    )
}

@Composable
private fun CameraPreviewView(
    imageCapture: ImageCapture,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    cameraUIAction: (CameraUIAction) -> Unit,
    onNavBack: () -> Unit,
    featureSelected: CameraFeature,
    onSelectCameraFeature: (CameraFeature) -> Unit,
    barcodeStatus: BarcodeDetectorStatus,
    textRecognition: TextRecognitionStatus,
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    val previewView = remember { PreviewView(context) }
    LaunchedEffect(lensFacing, lifecycleOwner) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    CameraOptions(
        previewView = previewView,
        textRecognition = textRecognition,
        barcodeStatus = barcodeStatus,
        cameraUIAction = cameraUIAction,
        onNavBack = onNavBack,
        onSelectCameraFeature = onSelectCameraFeature,
        featureSelected = featureSelected,
        messageInfo = when(featureSelected) {
            CameraFeature.QRReader -> {
                if (barcodeStatus is BarcodeDetectorStatus.Error)
                    "Invalid QR code. Try a new one or try again." else ""
            }
            CameraFeature.TextRecognizer -> {
                if (textRecognition is TextRecognitionStatus.Error)
                    "Text recognition error. Try a new text or try again." else ""
            }
        }
    )
}

@Composable
private fun CameraOptions(
    previewView: PreviewView,
    textRecognition: TextRecognitionStatus,
    cameraUIAction: (CameraUIAction) -> Unit,
    onNavBack: () -> Unit,
    featureSelected: CameraFeature,
    onSelectCameraFeature: (CameraFeature) -> Unit,
    barcodeStatus: BarcodeDetectorStatus,
    messageInfo: String,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        if (featureSelected == CameraFeature.QRReader) {
            val infiniteTransition = rememberInfiniteTransition()

            val colorLines by infiniteTransition.animateColor(
                initialValue = MaterialTheme.colors.surface.copy(alpha = 0.2f),
                targetValue = MaterialTheme.colors.surface,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            val maxSize by infiniteTransition.animateFloat(
                initialValue = 280f,
                targetValue = 300f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            val maxWidth by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 36f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Canvas(modifier = Modifier
                .size(maxSize.dp)
                .align(Alignment.Center)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val slideWidth = size.width / 4
                val slideHeight = size.height / 4

                val lineTopLeft = listOf(
                    Offset(0f, slideHeight),
                    Offset(0f, 0f),
                    Offset(slideWidth, 0f),
                )

                val lineTopRight = listOf(
                    Offset(slideWidth * 3, 0f),
                    Offset(canvasWidth, 0f),
                    Offset(canvasWidth, slideHeight),
                )

                val lineBottomLeft = listOf(
                    Offset(0f, slideWidth * 3),
                    Offset(0f, canvasHeight),
                    Offset(slideWidth, canvasHeight),
                )

                val lineBottomRight = listOf(
                    Offset(slideWidth * 3, canvasHeight),
                    Offset(canvasWidth, canvasHeight),
                    Offset(canvasHeight, slideHeight * 3),
                )

                listOf(lineTopLeft, lineTopRight, lineBottomLeft, lineBottomRight).forEach {
                    drawPoints(
                        points = it,
                        pointMode = PointMode.Polygon,
                        color = colorLines,
                        strokeWidth = maxWidth,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
        HazelToolbarCamera(
            onGallery = { cameraUIAction(CameraUIAction.OnGalleryViewClick) },
            onNavBack = onNavBack,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val width = when {
            screenWidth > 500 -> 190.dp
            else -> screenWidth.dp
        }
        val align = when {
            screenWidth > 500 -> Alignment.BottomEnd
            else -> Alignment.BottomCenter
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .align(align)
                .width(width)
                .padding(8.dp)
                .background(
                    color = Color.Black.copy(0.4f),
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            when {
                screenWidth > 500 -> MenuCameraColumn(
                    featureSelected = featureSelected,
                    onSelectCameraFeature = onSelectCameraFeature,
                )
                else -> MenuCamera(
                    featureSelected = featureSelected,
                    onSelectCameraFeature = onSelectCameraFeature,
                    messageInfo = messageInfo,
                )
            }
            when (featureSelected) {
                CameraFeature.QRReader ->
                    CameraControls(cameraUIAction, barcodeStatus)
                CameraFeature.TextRecognizer ->
                    CameraControls(cameraUIAction, textRecognition)
            }
        }
    }
}

@Composable
fun MenuCamera(
    featureSelected: CameraFeature,
    onSelectCameraFeature: (CameraFeature) -> Unit,
    messageInfo: String,
) {
    Column(modifier = Modifier
        .background(
            color = MaterialTheme.colors.secondary.copy(alpha = 0.4f),
            shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            listOf(
                CameraFeature.TextRecognizer,
                CameraFeature.QRReader
            ).forEach { cameraFeature ->
                SimpleChip(
                    text = when(cameraFeature) {
                        CameraFeature.QRReader -> "QR Reader"
                        CameraFeature.TextRecognizer -> "Text recognizer"
                    },
                    selected = featureSelected == cameraFeature,
                    onClick = { onSelectCameraFeature(cameraFeature) },
                )
            }
        }

        AnimatedVisibility(visible = messageInfo.isNotEmpty()) {
            Text(
                text =  messageInfo,
                style = MaterialTheme.typography.caption,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun MenuCameraColumn(
    featureSelected: CameraFeature,
    onSelectCameraFeature: (CameraFeature) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.secondary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            )
            .fillMaxWidth()
    ) {
        listOf(
            CameraFeature.TextRecognizer,
            CameraFeature.QRReader
        ).forEach { cameraFeature ->
            SimpleChip(
                text = when(cameraFeature) {
                    CameraFeature.QRReader -> "QR Reader"
                    CameraFeature.TextRecognizer -> "Text recognizer"
                },
                selected = featureSelected == cameraFeature,
                onClick = { onSelectCameraFeature(cameraFeature) },
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraOptionsPreview() {
    val localContext = LocalContext.current
    HazelTheme(colorVariant = ColorVariant.Green) {
        CameraOptions(
            previewView = PreviewView(localContext),
            textRecognition = TextRecognitionStatus.Normal,
            cameraUIAction = {},
            onNavBack = {},
            featureSelected = CameraFeature.QRReader,
            onSelectCameraFeature = {},
            barcodeStatus = BarcodeDetectorStatus.Normal,
            messageInfo = ""
        )
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

sealed class CameraUIAction {
    object OnCameraClick : CameraUIAction()
    object OnGalleryViewClick : CameraUIAction()
    object OnSwitchCameraClick : CameraUIAction()
}

sealed class CameraFeature(val name: String) {
    object TextRecognizer: CameraFeature("text_recognizer")
    object QRReader: CameraFeature("qr_reader")

    companion object  {
        fun fromString(value: String):CameraFeature {
            return when(value) {
                TextRecognizer.name -> TextRecognizer
                QRReader.name -> QRReader
                else -> TextRecognizer
            }
        }
    }
}