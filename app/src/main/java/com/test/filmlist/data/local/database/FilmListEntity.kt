package com.test.filmlist.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film_list")
data class FilmListEntity(
    @PrimaryKey val nameRu: String,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val year: Int?,
    val description: String?,
    val genresList: String?,
    val countriesList: String?,
    val isFavorite: Boolean = false
)