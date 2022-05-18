package com.github.calo001.hazel.ui.widget

import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.github.calo001.hazel.R
import com.github.calo001.hazel.model.hazeldb.HazelContent
import com.github.calo001.hazel.model.widget.HazelSimpleItem
import com.github.calo001.hazel.model.widget.getSimpleItemList
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.IOException

const val WIDGET_ACTION_SECTION = "widget_type"


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
class HazelWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HazelWidget()
}


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class HazelWidget: GlanceAppWidget() {
    companion object {
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(150.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }

    private var hazelContent: List<HazelSimpleItem> = listOf()
    override val sizeMode: SizeMode get() = SizeMode.Responsive(sizes = setOf(
            SMALL_SQUARE, HORIZONTAL_RECTANGLE, BIG_SQUARE
        )
    )

    private fun initHazel(context: Context, typeContent: String) {
        val hazelDb = context.resources?.openRawResource(R.raw.hazel)
        val outputStream = ByteArrayOutputStream()

        val buf = ByteArray(1024)
        var len: Int
        try {
            while (hazelDb?.read(buf).also { len = it ?: -1 } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            hazelDb?.close()

            val content = outputStream.toString()
            val data = Json.decodeFromString<HazelContent>(content)
            hazelContent = data.getSimpleItemList(typeContent).take(90)
        } catch (e: IOException) { }
    }

    @Composable
    override fun Content() {
        val size = LocalSize.current
        val context = LocalContext.current

        val preferences = context.hazelAppPreferences
        val typeContent = preferences.get(WIDGET_ACTION_SECTION, Routes.Animals.name)
        initHazel(context, typeContent)
        val painterIdentifier = PainterIdentifier(
            resources = context.resources,
            packageName = context.packageName,
            default = R.drawable.ic_launcher_foreground
        )

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
        ) {
            Image(
                provider = ImageProvider(R.drawable.widget_background),
                contentDescription = null,
                modifier = GlanceModifier.fillMaxSize()
            )

            WidgetListContent(painterIdentifier, size)

            HeaderHazelWidget()

            if (size.height == BIG_SQUARE.height &&
                size.width == BIG_SQUARE.width) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = GlanceModifier.fillMaxSize()
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.widget_bottom_transparent),
                        contentDescription = null,
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                    Row(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val routes = listOf(
                            Routes.Colors,
                            Routes.Countries,
                            Routes.Animals,
                            Routes.VerbsRegular,
                            Routes.VerbsIrregular,
                        )

                        routes.forEach { route ->
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                if (typeContent == route.name) {
                                    Image(
                                        provider = ImageProvider(R.drawable.widget_selected_option),
                                        contentDescription = null,
                                        modifier = GlanceModifier
                                            .size(36.dp)
                                    )
                                }
                                Image(
                                    provider = ImageProvider(route.icon),
                                    contentDescription = null,
                                    modifier = GlanceModifier
                                        .padding(16.dp)
                                        .clickable(actionRunCallback<ChangeSectionCallback>(
                                            actionParametersOf(
                                                ActionParameters.Key<String>(WIDGET_ACTION_SECTION) to route.name
                                            )
                                        ))
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = GlanceModifier.fillMaxSize()
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.widget_bottom_transparent),
                        contentDescription = null,
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(40.dp)
                    )
                }
            }
        }
    }


    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @Composable
    private fun HeaderHazelWidget() {
        Box {
            Image(
                provider = ImageProvider(R.drawable.widget_top_transparent),
                contentDescription = null,
                modifier = GlanceModifier
                    .fillMaxSize()
                    .height(60.dp)
            )
            Image(
                provider = ImageProvider(R.drawable.ic_hazel_logo_ext_dark),
                contentDescription = null,
                modifier = GlanceModifier
                    //.clickable(actionLaunchActivity<MainActivity>())
                    .padding(8.dp)
                    .height(52.dp)
            )
        }
    }

    @Composable
    private fun WidgetListContent(
        painterIdentifier: PainterIdentifier,
        size: DpSize
    ) {
        val spaceSize = if (size == SMALL_SQUARE) 6.dp else 10.dp
        LazyColumn {
            item {
                Spacer(modifier = GlanceModifier.size(60.dp))
            }
            items(count = hazelContent.size) { index ->
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .padding(horizontal = 16.dp)
//                        .clickable(actionLaunchActivity<MainActivity>(
//                            actionParametersOf(
//                                ActionParameters.Key<String>(WIDGET_ACTION_SECTION) to hazelContent[index].route
//                            )
//                        ))
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.widget_item_background),
                        contentDescription = null,
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .height(56.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.fillMaxHeight()
                    ) {
                        hazelContent[index].image?.let {
                            Box(
                                modifier = GlanceModifier

                            ) {
                                Image(
                                    provider = ImageProvider(painterIdentifier.getPainterRes(it)),
                                    contentDescription = null,
                                    modifier = GlanceModifier
                                        .fillMaxSize()
                                        .size(42.dp)
                                        .padding(horizontal = spaceSize)
                                )
                            }
                        }
                        hazelContent[index].color?.let { color ->
                            Spacer(modifier = GlanceModifier.size(spaceSize))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = GlanceModifier
                                    .size(22.dp)
                                    .background(Color.Black)
                            ) {
                                Box(
                                    modifier = GlanceModifier
                                        .size(20.dp)
                                        .background(Color.parse(color))
                                ) { }
                            }
                            Spacer(modifier = GlanceModifier.size(spaceSize))
                        }

                        val length = hazelContent[index].name.length

                        Text(
                            text = hazelContent[index].name,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = if(size == SMALL_SQUARE) {
                                    calculateTextSize(length)
                                } else { 18.sp },
                                textAlign = TextAlign.Center
                            ),
                            modifier = GlanceModifier
                                .padding(vertical = spaceSize)
                                .padding(end = spaceSize)
                                .wrapContentHeight()
                        )
                    }
                }
            }

            val endSpace = if (size == BIG_SQUARE) 60.dp else 24.dp
            item {
                Spacer(modifier = GlanceModifier.size(endSpace))
            }
        }
    }

    private fun calculateTextSize(length: Int): TextUnit =
        when {
            length > 12 -> 12.sp
            length > 7 -> 14.sp
            else -> 18.sp
        }

}


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
class ChangeSectionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val preferences = context.hazelAppPreferences
        try {
            val param = parameters[ActionParameters.Key(WIDGET_ACTION_SECTION)]
                ?: Routes.Animals.name
            preferences[WIDGET_ACTION_SECTION] = param
            HazelWidget().update(context, glanceId)
        } catch (e: Exception) {
            Log.i("MyCallback", e.message ?: "uknow error")
        }
    }
}
