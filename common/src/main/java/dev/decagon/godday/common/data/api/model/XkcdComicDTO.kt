package dev.decagon.godday.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class XkcdComicDTO(
    val month: String = "",
    @Json(name = "num") val id: Long = 0L,
    val year: String = "",
    val title: String = "",
    val day: String = "",
    @Json(name = "img") val imageUrl: String = "",
    @Json(name = "alt") val subText: String = "",
    @Json(name = "transcript") val description: String? = ""
)