package com.github.calo001.hazel.model.hazeldb


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName("id")
    val id: String,
    @SerialName("country_phonetic")
    val countryPhonetic: String, // /ðə juˌnaɪtɪd ˈkɪŋdəm/
    @SerialName("emoji_code")
    val emojiCode: String, // 1F1EC-1F1E7
    @SerialName("language")
    val language: String, // English
    @SerialName("language_phonetic")
    val languagePhonetic: String, // /ˈɪŋɡlɪʃ/
    @SerialName("link_maps")
    val linkMaps: String, // https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwj3hPOXpPvzAhUfmGoFHXyFB3oQFnoECAIQAQ&url=https%3A%2F%2Fwww.google.com%2Fmaps%2Fsearch%2F%3Fapi%3D1%26query%3DUnited%2520Kingdom%252C%2520England%26query_place_id%3DChIJn524nBcQdkgR1qhQOpSwVTA&usg=AOvVaw2aisBjlwuh8amecWNHQDH0
    @SerialName("name")
    val name: String, // United Kingdom
    @SerialName("nationality")
    val nationality: String, // British
    @SerialName("nationality_phonetic")
    val nationalityPhonetic: String, // /ˈbrɪtɪʃ/
    @SerialName("latitude")
    val latitude: String, // /ˈbrɪtɪʃ/
    @SerialName("longitude")
    val longitude: String, // /ˈbrɪtɪʃ/
    @SerialName("zoom")
    val zoom: Int, // /ˈbrɪtɪʃ/
)