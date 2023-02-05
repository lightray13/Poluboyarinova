package com.test.filmlist.data.repository.film

import com.test.filmlist.api.Result
import com.test.filmlist.data.local.database.FilmListEntity
import com.test.filmlist.util.Constants
import javax.inject.Inject

class FilmRepository @Inject constructor(
    private val filmRemoveDataSource: FilmRemoveDataSource,
    private val filmLocalDataSource: FilmLocalDataSource
) {
    fun filmById(nameId: String) = filmLocalDataSource.filmById(nameId)

    suspend fun loadFilmById(nameId: String) {
        val result = filmRemoveDataSource.filmById(nameId)
        when(result) {
            is Result.Success -> {
                val favoriteFilmIds = filmLocalDataSource.favoriteFilmIds()
                result.data.let { film ->
                    val filmListEntity = FilmListEntity(
                        film.nameRu ?: "",
                        film.posterUrl,
                        film.posterUrlPreview,
                        film.year,
                        film.description,
                        film.genres.toString(),
                        film.countries.toString(),
                        favoriteFilmIds.contains(film.nameRu)
                    )
                    filmLocalDataSource.updateFilm(filmListEntity)
                }
                Result.Success(true)
            }
            is Result.Error -> Result.Error(Constants.GENERIC_ERROR)
        }
    }
}