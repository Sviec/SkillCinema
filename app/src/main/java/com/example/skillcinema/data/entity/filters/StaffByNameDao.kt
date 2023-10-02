package com.example.skillcinema.data.entity.filters

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffByNameDao(
    @Json(name = "total") val total: Int,
    @Json(name = "items") val items: List<StaffDao>
)

