package com.example.skillcinema.data.entity.staffById

import com.example.skillcinema.entity.CountryGenre
import com.example.skillcinema.entity.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffByIdFilmDao(
    @Json(name = "filmId") override val filmId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "rating") val ratingStr: String?,
    @Json(name = "general") val general: Boolean?,
    @Json(name = "description") val description: String?,
    @Json(name = "professionKey") val professionKey: String?
) : Film {
    override val posterUrlPreview: String = ""
    override val name: String = nameRu ?: (nameEn ?: "Unknown")
    override val rating: Float? = ratingStr?.toFloat()
    override val genres: List<CountryGenre> = emptyList()
}
