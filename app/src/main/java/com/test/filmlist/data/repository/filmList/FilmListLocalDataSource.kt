package com.test.filmlist.data.repository.filmList

import androidx.lifecycle.LiveData
import com.test.filmlist.data.local.database.FilmDatabase
import com.test.filmlist.data.local.database.FilmListEntity
import javax.inject.Inject

class FilmListLocalDataSource @Inject constructor(private val database: FilmDatabase) {

    val filmList: LiveData<List<FilmListEntity>> = database.filmListDao().filmList()
    val filmFavoriteList: LiveData<List<FilmListEntity>> = database.filmListDao().favoriteFilms()

    suspend fun insertFilmListIntoDatabase(filmList: List<FilmListEntity>) {
        if (filmList.isNotEmpty()) {
            database.filmListDao().insert(filmList)
        }
    }

    suspend fun favoriteFilmIds(): List<String> {
        return database.filmListDao().favoriteFilmIds()
    }

    suspend fun updateFilmFavoriteStatus(nameId: String): FilmListEntity? {
        val film = database.filmListDao().filmByFilmId(nameId)
        film?.let {
            val filmListEntity = FilmListEntity(
                it.nameRu,
                it.posterUrl,
                it.posterUrlPreview,
                it.year,
                it.description,
                it.genresList,
                it.countriesList,
                it.isFavorite.not()
            )
            if (database.filmListDao().updateFilmListEntity(filmListEntity) > 0) {
                return filmListEntity
            }
        }
        return null
    }
}