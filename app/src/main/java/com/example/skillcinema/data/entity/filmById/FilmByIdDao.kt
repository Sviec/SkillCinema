package com.example.skillcinema.data.entity.filmById

import com.example.skillcinema.data.entity.CountryDao
import com.example.skillcinema.data.entity.GenreDao
import com.example.skillcinema.entity.FilmById
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmByIdDao(
    @Json(name = "kinopoiskId") override val kinopoiskId: Int,
    @Json(name = "imdbId") val imdbId: String?,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "posterUrl") override val posterUrl: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String,
    @Json(name = "coverUrl") val coverUrl: String?,
    @Json(name = "logoUrl") val logoUrl: String?,
    @Json(name = "reviewsCount") val reviewsCount: Int?,
    @Json(name = "ratingGoodReview") val ratingGoodReview: Float?,
    @Json(name = "ratingGoodReviewVoteCount") val ratingGoodReviewVoteCount: Int?,
    @Json(name = "ratingKinopoisk") override val ratingKinopoisk: Float?,
    @Json(name = "ratingKinopoiskVoteCount") val ratingKinopoiskVoteCount: Int?,
    @Json(name = "ratingImdb") val ratingImdb: Float?,
    @Json(name = "ratingImdbVoteCount") val ratingImdbVoteCount: Int?,
    @Json(name = "ratingFilmCritics") val ratingFilmCritics: Float?,
    @Json(name = "ratingFilmCriticsVoteCount") val ratingFilmCriticsVoteCount: Int?,
    @Json(name = "ratingAwait") val ratingAwait: Float?,
    @Json(name = "ratingAwaitCount") val ratingAwaitCount: Int?,
    @Json(name = "ratingRfCritics") val ratingRfCritics: Float?,
    @Json(name = "ratingRfCriticsVoteCount") val ratingRfCriticsVoteCount: Int?,
    @Json(name = "webUrl") val webUrl: String?,
    @Json(name = "year") override val year: Int,
    @Json(name = "filmLength") override val filmLength: Int?,
    @Json(name = "slogan") val slogan: String?,
    @Json(name = "description") override val description: String?,
    @Json(name = "shortDescription") override val shortDescription: String?,
    @Json(name = "editorAnnotation") val editorAnnotation: String?,
    @Json(name = "isTicketsAvailable") val isTicketsAvailable: Boolean?,
    @Json(name = "productionStatus") val productionStatus: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "ratingMpaa") val ratingMpaa: String?,
    @Json(name = "ratingAgeLimits") val ratingAgeLimits: String?,
    @Json(name = "hasImax") val hasImax: Boolean?,
    @Json(name = "has3D") val has3D: Boolean?,
    @Json(name = "lastSync") val lastSync: String?,
    @Json(name = "countries") override val countries: List<CountryDao>,
    @Json(name = "genres") override val genres: List<GenreDao>,
    @Json(name = "startYear") val startYear: Int?,
    @Json(name = "endYear") val endYear: Int?,
    @Json(name = "serial") override val serial: Boolean,
    @Json(name = "shortFilm") val shortFilm: Boolean?,
    @Json(name = "completed") val completed: Boolean?
) : FilmById {
    override val name: String = nameRu ?: (nameOriginal ?: (nameEn ?: "Unknown"))
}
