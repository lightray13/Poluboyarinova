package com.test.filmlist.ui.film

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.test.filmlist.data.local.database.FilmListEntity
import com.test.filmlist.databinding.ActivityFilmBinding
import com.test.filmlist.util.Constants
import com.test.filmlist.util.ImageLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmActivity : AppCompatActivity() {
    private val viewModel: FilmViewModel by viewModels()

    private lateinit var binding: ActivityFilmBinding

    private var filmId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (intent?.hasExtra(Constants.EXTRA_FILM_ID) == true) {
            filmId = intent.getStringExtra(Constants.EXTRA_FILM_ID)
        }

        filmId?.let { viewModel.loadFilmFromApi(it) }

        observeViewModel()
    }

    private fun observeViewModel() {
        filmId?.let {
            viewModel.isLoading.observe(this) { isLoading ->
                binding.filmListLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
            }

            viewModel.filmById(it).observe(this) { film ->
                initializeViews(film)
            }
        }
    }

    private fun initializeViews(film: FilmListEntity) {
        with(binding) {
            ImageLoader.loadImage(filmImageView, film.posterUrl ?: "")
            filmNameTextView.text = film.nameRu
            filmDescriptionTextView.text = film.description
            filmGenreTextView.text = film.genresList
            filmCountryTextView.text = film.countriesList
            onBackPressedButton.setOnClickListener {
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}