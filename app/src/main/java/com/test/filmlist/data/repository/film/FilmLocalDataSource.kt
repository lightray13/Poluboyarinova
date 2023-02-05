package com.test.filmlist.data.repository.film

import androidx.lifecycle.LiveData
import com.test.filmlist.data.local.database.FilmDatabase
import com.test.filmlist.data.local.database.FilmListEntity
import javax.inject.Inject

class FilmLocalDataSource @Inject constructor(private val database: FilmDatabase) {

    fun filmById(nameId: String): LiveData<FilmListEntity> {
        return database.filmListDao().filmLiveDataById(nameId)
    }

    suspend fun favoriteFilmIds(): List<String> {
        return database.filmListDao().favoriteFilmIds()
    }

    suspend fun updateFilm(film: FilmListEntity): FilmListEntity? {
        if (database.filmListDao().updateFilmListEntity(film) > 0) {
            return film
        }
        return null
    }
}