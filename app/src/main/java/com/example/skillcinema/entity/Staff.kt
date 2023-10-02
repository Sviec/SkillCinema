package com.example.skillcinema.entity

interface Staff {
    val staffId: Int
    val name: String
    val posterUrl: String?
    val description: String?
    val professionKey: String
}