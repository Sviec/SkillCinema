package com.example.skillcinema

import android.app.Application
import androidx.room.Room
import com.example.skillcinema.data.db.AppDatabase

class App : Application() {
    lateinit var db: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room
            .databaseBuilder(
                this,
                AppDatabase::class.java,
                "db"
            ).build()
    }

}