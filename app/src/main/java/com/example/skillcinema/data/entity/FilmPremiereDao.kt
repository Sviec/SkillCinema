package com.example.skillcinema.data.entity

import com.example.skillcinema.entity.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmPremiereDao(
    @Json(name = "kinopoiskId") val kinopoiskId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "year") val year: Int,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") override val posterUrlPreview: String,
    @Json(name = "countries") val countries: List<CountryDao>,
    @Json(name = "genres") override val genres: List<GenreDao>,
    @Json(name = "duration") val duration: Int?,
    @Json(name = "premiereRu") val premiereRu: String
) : Film {
    override val name: String = nameRu ?: nameEn ?: "Unknown"
    override val filmId: Int = kinopoiskId
    override val rating: Float? = null
}