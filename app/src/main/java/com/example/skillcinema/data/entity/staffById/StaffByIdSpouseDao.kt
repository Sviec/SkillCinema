package com.example.skillcinema.data.entity.staffById

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffByIdSpouseDao(
    @Json(name = "personId") val personId: Int,
    @Json(name = "name") val name: String,
    @Json(name = "divorced") val divorced: Boolean,
    @Json(name = "divorcedReason") val divorcedReason: String,
    @Json(name = "sex") val sex: String,
    @Json(name = "children") val children: Int?,
    @Json(name = "webUrl") val webUrl: String?,
    @Json(name = "relation") val relation: String
)
