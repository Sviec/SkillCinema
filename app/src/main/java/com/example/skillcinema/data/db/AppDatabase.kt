package com.example.skillcinema.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Film::class,
        Collection::class,
        FilmCollection::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun filmCollectionDao(): FilmCollectionDao
    abstract fun collectionDao(): CollectionDao
}