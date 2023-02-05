package com.test.filmlist.data.repository.filmList

import android.text.TextUtils
import com.test.filmlist.api.Result
import com.test.filmlist.api.model.Country
import com.test.filmlist.api.model.Genre
import com.test.filmlist.data.local.database.FilmListEntity
import com.test.filmlist.util.Constants
import javax.inject.Inject

class FilmListRepository @Inject constructor(
    private val filmListRemoveDataSource: FilmListRemoveDataSource,
    private val filmListLocalDataSource: FilmListLocalDataSource
    ) {

    val filmList = filmListLocalDataSource.filmList
    val filmFavoriteList = filmListLocalDataSource.filmFavoriteList

    suspend fun loadFilmList(type: String) {
        val result = filmListRemoveDataSource.filmList(type)
        when(result) {
            is Result.Success -> {
                val favoriteFilmIds = filmListLocalDataSource.favoriteFilmIds()
                val filmList = result.data.films?.let {
                   it.filter { item -> item.nameRu.isNullOrEmpty().not() }
                       .map { film ->
                           FilmListEntity(
                               film.nameRu ?: "",
                               film.posterUrl,
                               film.posterUrlPreview,
                               film.year,
                               film.description,
                               genresToString(film.genres),
                               countriesToString(film.countries),
                               favoriteFilmIds.contains(film.nameRu)
                           )
                       }
                }
                filmList?.let { filmListLocalDataSource.insertFilmListIntoDatabase(it) }
                Result.Success(true)
            }
            is Result.Error -> Result.Error(Constants.GENERIC_ERROR)
        }
    }

    suspend fun updateFilmFavoriteStatus(nameId: String): Result<FilmListEntity> {
        val result = filmListLocalDataSource.updateFilmFavoriteStatus(nameId)
        return result?.let { Result.Success(it) } ?: Result.Error(Constants.GENERIC_ERROR)
    }

    private fun genresToString(genres: List<Genre>?): String {
        return genres?.let {
            val genresTextItems = genres.map { it.genre }
            TextUtils.join(", ", genresTextItems) } ?: ""
    }

    private fun countriesToString(countries: List<Country>?): String {
        return countries?.let {
            val countriesTextItems = countries.map { it.country }
            TextUtils.join(", ", countriesTextItems) } ?: ""
    }

}