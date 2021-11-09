package com.github.calo001.hazel.model.unsplash


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Urls(
    @SerialName("full")
    val full: String, // https://images.unsplash.com/photo-1588345921523-c2dcdb7f1dcd?crop=entropy&cs=srgb&fm=jpg&ixid=MnwyNzQ0MDN8MHwxfHNlYXJjaHwxfHx3aGl0ZXxlbnwwfHx8fDE2MzY0OTEzNTk&ixlib=rb-1.2.1&q=85
    @SerialName("raw")
    val raw: String, // https://images.unsplash.com/photo-1588345921523-c2dcdb7f1dcd?ixid=MnwyNzQ0MDN8MHwxfHNlYXJjaHwxfHx3aGl0ZXxlbnwwfHx8fDE2MzY0OTEzNTk&ixlib=rb-1.2.1
    @SerialName("regular")
    val regular: String, // https://images.unsplash.com/photo-1588345921523-c2dcdb7f1dcd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNzQ0MDN8MHwxfHNlYXJjaHwxfHx3aGl0ZXxlbnwwfHx8fDE2MzY0OTEzNTk&ixlib=rb-1.2.1&q=80&w=1080
    @SerialName("small")
    val small: String, // https://images.unsplash.com/photo-1588345921523-c2dcdb7f1dcd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNzQ0MDN8MHwxfHNlYXJjaHwxfHx3aGl0ZXxlbnwwfHx8fDE2MzY0OTEzNTk&ixlib=rb-1.2.1&q=80&w=400
    @SerialName("thumb")
    val thumb: String // https://images.unsplash.com/photo-1588345921523-c2dcdb7f1dcd?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNzQ0MDN8MHwxfHNlYXJjaHwxfHx3aGl0ZXxlbnwwfHx8fDE2MzY0OTEzNTk&ixlib=rb-1.2.1&q=80&w=200
)