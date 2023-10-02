package com.example.skillcinema.data.entity.filmById

import com.example.skillcinema.entity.Staff
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmByIdStaffDao(
    @Json(name = "staffId") override val staffId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "description") override val description: String?,
    @Json(name = "posterUrl") override val posterUrl: String?,
    @Json(name = "professionText") val professionText: String?,
    @Json(name = "professionKey") override val professionKey: String
) : Staff {
    override val name: String = nameRu ?: (nameEn ?: "Unknown")
}