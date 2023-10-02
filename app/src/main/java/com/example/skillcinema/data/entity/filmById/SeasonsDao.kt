package com.example.skillcinema.data.entity.filmById

import com.example.skillcinema.entity.Episode
import com.example.skillcinema.entity.Season
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonsDao(
    @Json(name = "total") val total: Int,
    @Json(name = "items") val items: List<SeasonDao>
)

@JsonClass(generateAdapter = true)
data class SeasonDao(
    @Json(name = "number") override val number: Int,
    @Json(name = "episodes") override val episodes: List<EpisodeDao>,
) : Season

@JsonClass(generateAdapter = true)
data class EpisodeDao(
    @Json(name = "seasonNumber") override val seasonNumber: Int,
    @Json(name = "episodeNumber") override val episodeNumber: Int,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "synopsis") val synopsis: String?,
    @Json(name = "releaseDate") override val releaseDate: String?,
) : Episode {
    override val name: String = nameRu ?: (nameEn ?: "Unknown")
}
