package com.example.skillcinema.data.entity.filters

import com.example.skillcinema.data.entity.CountryDao
import com.example.skillcinema.data.entity.GenreDao
import com.example.skillcinema.entity.GenresAndCountries
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FiltersGenresCountriesDao(
    @Json(name = "genres") override val genres: List<GenreDao>,
    @Json(name = "countries") override val countries: List<CountryDao>,
) : GenresAndCountries
