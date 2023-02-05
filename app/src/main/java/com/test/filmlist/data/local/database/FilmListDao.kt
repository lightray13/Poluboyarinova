package com.test.filmlist.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FilmListDao {

    @Query("SELECT * FROM film_list")
    fun filmList(): LiveData<List<FilmListEntity>>

    @Query("SELECT * FROM film_list WHERE nameRu = :name")
    suspend fun filmByFilmId(name: String): FilmListEntity?

    @Query("SELECT * FROM film_list WHERE nameRu = :name")
    fun filmLiveDataById(name: String): LiveData<FilmListEntity>

    @Query("SELECT nameRu FROM film_list WHERE isFavorite = 1")
    suspend fun favoriteFilmIds(): List<String>

    @Query("SELECT * FROM film_list WHERE isFavorite = 1")
    fun favoriteFilms(): LiveData<List<FilmListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<FilmListEntity>)

    @Update
    suspend fun updateFilmListEntity(data: FilmListEntity): Int
}