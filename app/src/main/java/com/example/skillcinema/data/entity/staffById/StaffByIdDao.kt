package com.example.skillcinema.data.entity.staffById

import com.example.skillcinema.entity.StaffById
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffByIdDao(
    @Json(name = "personId") override val personId: Int,
    @Json(name = "webUrl") val webUrl: String?,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "sex") val sex: String,
    @Json(name = "posterUrl") override val posterUrl: String?,
    @Json(name = "growth") val growth: Int?,
    @Json(name = "birthday") val birthday: String?,
    @Json(name = "death") val death: String?,
    @Json(name = "age") val age: Int?,
    @Json(name = "birthplace") val birthplace: String?,
    @Json(name = "deathplace") val deathplace: String?,
    @Json(name = "hasAwards") val hasAwards: Int?,
    @Json(name = "profession") override val profession: String,
    @Json(name = "facts") val facts: List<String>?,
    @Json(name = "spouses") val spouses: List<StaffByIdSpouseDao>?,
    @Json(name = "films") override val films: List<StaffByIdFilmDao>
) : StaffById {
    override val name: String = nameRu ?: (nameEn ?: "Unknown")
}

