package com.example.skillcinema.data.entity

import com.example.skillcinema.entity.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmDao(
    @Json(name = "filmId") override val filmId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "year") val year: String,
    @Json(name = "filmLength") val filmLength: String?,
    @Json(name = "countries") val countries: List<CountryDao>,
    @Json(name = "genres") override val genres: List<GenreDao>,
    @Json(name = "rating") val ratingStr: String?,
    @Json(name = "ratingVoteCount") val ratingVoteCount: Double,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "posterUrlPreview") override val posterUrlPreview: String,
    @Json(name = "ratingChange") val ratingChange: String?
) : Film {
    override val name: String = nameRu ?: (nameEn ?: "Unknown")
    override val rating = try {
        ratingStr?.toFloat()
    } catch (e: Exception) {
        null
    }
}