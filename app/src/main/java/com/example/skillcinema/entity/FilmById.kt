package com.example.skillcinema.entity

import com.example.skillcinema.data.entity.CountryDao
import com.example.skillcinema.data.entity.GenreDao

interface FilmById {
    val kinopoiskId: Int
    val name: String
    val posterUrl: String
    val ratingKinopoisk: Float?
    val filmLength: Int?
    val description: String?
    val shortDescription: String?
    val countries: List<CountryDao>
    val genres: List<GenreDao>
    val year: Int
    val serial: Boolean
}