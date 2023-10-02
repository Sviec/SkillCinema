package com.example.skillcinema.entity

interface Film {
    val filmId: Int
    val posterUrlPreview: String
    val rating: Float?
    val name: String
    val genres: List<CountryGenre>
}
