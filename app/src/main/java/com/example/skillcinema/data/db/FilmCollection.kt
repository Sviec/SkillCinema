package com.example.skillcinema.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films_collections")
data class FilmCollection(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long? = null,
    @ColumnInfo(name = "film_id")
    val filmId: Int,
    @ColumnInfo(name = "collection_name")
    val collectionName: String
)