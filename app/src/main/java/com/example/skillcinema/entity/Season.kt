package com.example.skillcinema.entity

interface Season {
    val number: Int
    val episodes: List<Episode>
}

interface Episode {
    val seasonNumber: Int
    val episodeNumber: Int
    val name: String
    val releaseDate: String?
}