package com.example.skillcinema.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class FilmsDao(
    @Json(name = "pagesCount") val pagesCount: Int,
    @Json(name = "films") val films: List<FilmDao>
)