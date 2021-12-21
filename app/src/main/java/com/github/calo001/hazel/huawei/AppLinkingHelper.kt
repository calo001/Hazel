package com.github.calo001.hazel.huawei

import android.content.Context
import android.net.Uri
import com.github.calo001.hazel.util.shareUrl
import com.huawei.agconnect.applinking.AppLinking

fun Context.getAppLinking(
    title: String,
    route: String,
    appLinkingHelper: AppLinkingHelper,
    onSuccess: (String) -> Unit,
    onError: () -> Unit,
) {
    appLinkingHelper.buildShortAppLinking(
        title = title,
        description = title,
        imageUrl = "https://raw.githubusercontent.com/calo001/Hazel/master/resources/summary.webp",
        url = route,
        onSuccessListener = { url ->
            onSuccess(url)
        },
        onErrorListener = {
            onError()
        }
    )
}

class AppLinkingHelper {
    fun buildShortAppLinking(
        title: String,
        description: String,
        imageUrl: String,
        url: String,
        onSuccessListener: (String) -> Unit,
        onErrorListener: () -> Unit,
    ) {
        getAppLinking(
            title = title,
            description = description,
            imageUrl = imageUrl,
            url = url
        ).buildShortAppLinking()
            .addOnSuccessListener { shortAppLinking ->
                onSuccessListener(shortAppLinking.shortUrl.toString())
            }
            .addOnFailureListener {
                onErrorListener()
            }
    }

    private fun getAppLinking(
        title: String,
        description: String,
        imageUrl: String,
        url: String,
    ): AppLinking.Builder = AppLinking.newBuilder()
        .setUriPrefix("https://hazel.dra.agconnect.link")
        .setDeepLink(Uri.parse(url))
        .setAndroidLinkInfo(
            AppLinking.AndroidLinkInfo.newBuilder()
                .setAndroidDeepLink(url)
                .build()
        )
        .setSocialCardInfo(
            AppLinking.SocialCardInfo.newBuilder()
                .setTitle(title)
                .setDescription(description)
                .setImageUrl(imageUrl)
                .build()
        )
        .setPreviewType(AppLinking.LinkingPreviewType.AppInfo)

}