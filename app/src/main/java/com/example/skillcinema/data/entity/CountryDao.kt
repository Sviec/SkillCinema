package com.example.skillcinema.data.entity

import com.example.skillcinema.entity.CountryGenre
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryDao(
    @Json(name = "id") override  val id: Int? = null,
    @Json(name = "country") override val name: String
) : CountryGenre
