package com.test.filmlist.api

import com.test.filmlist.api.model.Film
import com.test.filmlist.api.model.FilmList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @Headers("X-API-KEY: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("/api/v2.2/films/top")
    suspend fun filmList(
        @Query("type") type: String
    ): Response<FilmList>

    @Headers("X-API-KEY: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("/api/v2.2/films/top/{id}")
    suspend fun filmById(
        @Path("id") id: String
    ): Response<Film>
}