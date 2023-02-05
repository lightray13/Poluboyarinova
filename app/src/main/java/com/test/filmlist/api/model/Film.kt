package com.test.filmlist.api.model

data class Film(
    val nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val year: Int?,
    val description: String?,
    val genres: List<Genre>?,
    val countries: List<Country>?
)

data class Genre(
    val genre: String?
)

data class Country(
    val country: String?
)
