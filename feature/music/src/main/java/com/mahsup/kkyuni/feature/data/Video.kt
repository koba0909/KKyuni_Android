package com.mahsup.kkyuni.feature.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Video(
    @Json(name="kind") val kind: String,
    @Json(name="id") val id: VideoId,
    @Json(name="snippet") val snippet: VideoSnippet,
)