package com.example.skillcinema.data.entity.filters

import com.example.skillcinema.entity.Staff
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffDao(
    @Json(name = "kinopoiskId") val kinopoiskId: Int,
    @Json(name = "webUrl") val webUrl: String,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "sex") val sex: String,
    @Json(name = "posterUrl") override val posterUrl: String,
) : Staff {
    override val name: String = nameRu ?: (nameEn ?: "Unknown")
    override val staffId: Int = kinopoiskId
    override val description: String? = null
    override val professionKey: String = ""
}
