package com.example.skillcinema.entity
import com.example.skillcinema.data.db.Film

data class Collection(
    val name: String,
    val filmList: List<Film>
)