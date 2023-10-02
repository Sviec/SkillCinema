package com.example.skillcinema.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class Film(
    @PrimaryKey
    @ColumnInfo(name = "film_id")
    val filmId: Int,
    @ColumnInfo(name = "posterUrlPreview")
    val posterUrlPreview: String,
    @ColumnInfo(name = "rating")
    val rating: Float?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "genres")
    val genres: String,
    @ColumnInfo(name = "is_viewed")
    val isViewed: Boolean
)
