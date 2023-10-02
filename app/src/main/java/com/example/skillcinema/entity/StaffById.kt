package com.example.skillcinema.entity


interface StaffById {
    val personId: Int
    val name: String
    val profession: String
    val posterUrl: String?
    val films: List<Film>
}