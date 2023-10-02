package com.example.skillcinema.data.entity.filmById

import com.example.skillcinema.entity.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesDao(
    @Json(name = "total") val total: Int,
    @Json(name = "totalPages") val totalPages: Int,
    @Json(name = "items") val items: List<ImageDao>
)

@JsonClass(generateAdapter = true)
data class ImageDao(
    @Json(name = "imageUrl") override val imageUrl: String,
    @Json(name = "previewUrl") override val previewUrl: String
) : Image