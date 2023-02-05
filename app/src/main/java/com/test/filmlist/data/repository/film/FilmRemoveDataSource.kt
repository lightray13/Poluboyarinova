package com.test.filmlist.data.repository.film

import com.test.filmlist.api.ApiInterface
import com.test.filmlist.api.BaseRemoteDataSource
import com.test.filmlist.api.model.Film
import com.test.filmlist.api.Result
import javax.inject.Inject

class FilmRemoveDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {

    suspend fun filmById(nameId: String): Result<Film> {
        return getResult { service.filmById(nameId) }
    }
}