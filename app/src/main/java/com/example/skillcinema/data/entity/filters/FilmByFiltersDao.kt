package com.example.skillcinema.data.entity.filters

import com.example.skillcinema.data.entity.CountryDao
import com.example.skillcinema.data.entity.GenreDao
import com.example.skillcinema.entity.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmByFiltersDao(
    @Json(name = "kinopoiskId") val kinopoiskId: Int,
    @Json(name = "imdbId") val imdbId: String?,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "countries") val countries: List<CountryDao>,
    @Json(name = "genres") override val genres: List<GenreDao>,
    @Json(name = "ratingKinopoisk") val ratingKinopoisk: Float?,
    @Json(name = "ratingImdb") val ratingImdb: Float?,
    @Json(name = "year") val year: Int?,
    @Json(name = "type") val type: String,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") override val posterUrlPreview: String,
) : Film {
    override val name: String = nameRu ?: (nameEn ?: (nameOriginal ?: "Unknown"))
    override val filmId: Int = kinopoiskId
    override val rating: Float? = ratingKinopoisk
}
