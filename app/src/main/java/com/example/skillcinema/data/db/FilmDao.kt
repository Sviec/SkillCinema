package com.example.skillcinema.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Query("SELECT * FROM films")
    fun getAll(): Flow<List<Film>>
    @Insert
    suspend fun insert(film: Film)
    @Update
    suspend fun update(film: Film)
    @Query("DELETE FROM films WHERE film_id == :filmId")
    suspend fun delete(filmId: Int)
    @Query("SELECT * FROM films WHERE film_id == :filmId")
    suspend fun findFilm(filmId: Int): List<Film>
    @Query("SELECT * FROM films WHERE is_viewed == 1")
    fun getViewed(): Flow<List<Film>>
}