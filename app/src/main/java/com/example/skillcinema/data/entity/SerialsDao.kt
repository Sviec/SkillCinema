package com.example.skillcinema.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerialsDao(
    @Json(name = "total") val total: Int,
    @Json(name = "totalPages") val totalPages: Int,
    @Json(name = "items") val items: List<SeriesDao>,
)