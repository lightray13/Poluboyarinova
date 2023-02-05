package com.test.filmlist.data.repository.filmList

import com.test.filmlist.api.ApiInterface
import com.test.filmlist.api.BaseRemoteDataSource
import com.test.filmlist.api.Result
import com.test.filmlist.api.model.FilmList
import javax.inject.Inject

class FilmListRemoveDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {

    suspend fun filmList(type: String): Result<FilmList> {
        return getResult { service.filmList(type) }
    }
}