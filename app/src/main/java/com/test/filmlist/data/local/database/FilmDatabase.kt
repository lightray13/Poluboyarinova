package com.test.filmlist.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FilmListEntity::class], version = 1, exportSchema = false)
abstract class FilmDatabase: RoomDatabase() {
    abstract fun filmListDao(): FilmListDao

    companion object {
        fun buildDatabase(context: Context): FilmDatabase {
            return Room.databaseBuilder(context, FilmDatabase::class.java, "Films").build()
        }
    }
}