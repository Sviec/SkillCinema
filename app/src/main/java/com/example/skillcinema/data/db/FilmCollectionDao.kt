package com.example.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmCollectionDao {
    @Query("SELECT * FROM films_collections WHERE collection_name == :collectionName")
    suspend fun getCollection(collectionName: String): List<FilmCollection>
    @Insert
    suspend fun insert(film: FilmCollection)
    @Delete
    suspend fun delete(film: FilmCollection)
    @Query("DELETE FROM films_collections WHERE film_id == :filmId AND collection_name == :collectionName")
    suspend fun deleteFilmFromCollection(filmId: Int, collectionName: String)
    @Query("DELETE FROM films_collections WHERE collection_name == :collectionName")
    suspend fun deleteCollection(collectionName: String)

    @Query("SELECT * FROM films_collections WHERE film_id == :filmId")
    suspend fun findFilm(filmId: Int): List<FilmCollection>
    @Query("SELECT * FROM films_collections WHERE collection_name == :collectionName")
    suspend fun findCollection(collectionName: String): List<FilmCollection>
}