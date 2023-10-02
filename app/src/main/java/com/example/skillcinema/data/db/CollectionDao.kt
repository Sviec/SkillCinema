package com.example.skillcinema.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections")
    fun getAll(): Flow<List<Collection>>
    @Insert
    suspend fun insert(collection: Collection)
    @Update
    suspend fun update(collection: Collection)
    @Delete
    suspend fun delete(collection: Collection)
    @Query("SELECT * FROM collections WHERE name == :collectionName")
    suspend fun getCollection(collectionName: String): List<Collection>
}