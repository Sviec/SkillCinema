package com.example.skillcinema.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "films_count")
    val filmsCount: Int,
    @ColumnInfo(name = "default")
    val mainCollection: Boolean,
    @ColumnInfo(name = "image")
    val image: String
)
