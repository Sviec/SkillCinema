package com.example.skillcinema.data.entity.filmById

import com.example.skillcinema.entity.CountryGenre
import com.example.skillcinema.entity.Film
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimilarsDao(
    @Json(name = "total") val total: Int,
    @Json(name = "items") val items: List<SimilarsDetailDao>
)

@JsonClass(generateAdapter = true)
data class SimilarsDetailDao(
    @Json(name = "filmId") override val filmId: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "posterUrl") val posterUrl: String?,
    @Json(name = "posterUrlPreview") override val posterUrlPreview: String,
    @Json(name = "relationType") val relationType: String,
) : Film {
    override val rating: Float? = null
    override val name: String = nameRu ?: (nameOriginal ?: (nameEn ?: "Unknown"))
    override val genres: List<CountryGenre> = emptyList()
}
