package com.github.calo001.hazel.providers

fun getAppLinking(
    title: String,
    route: String,
    appLinkingHelper: AppLinkingHelper,
    onSuccess: (String) -> Unit,
    onError: () -> Unit,
) {
    appLinkingHelper.buildShortAppLinking(
        title = title,
        description = title,
        imageUrl = "https://calo001.github.io/hazel-web/images/HazelLogoExtDark.png",
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
        onSuccessListener(url)
    }
}