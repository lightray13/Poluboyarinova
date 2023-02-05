package com.test.filmlist.ui.filmList

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.filmlist.R
import com.test.filmlist.adapter.FilmListAdapter
import com.test.filmlist.adapter.OnItemClickCallback
import com.test.filmlist.data.local.database.FilmListEntity
import com.test.filmlist.databinding.ActivityMainBinding
import com.test.filmlist.ui.film.FilmActivity
import com.test.filmlist.util.Constants
import com.test.filmlist.util.SearchHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnItemClickCallback {
    private val viewModel: FilmListViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val filmListAdapter = FilmListAdapter(this)

    private var newTasksList = mutableListOf<FilmListEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState != null) {
            viewModel.setFilmListStatus(savedInstanceState.getInt(Constants.ARG_FILM_STATUS))
        }
        viewModel.loadFilmsFromApi()

        observeViewModel()
        initializeViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.ARG_FILM_STATUS, viewModel.getFilmListStatus())
    }

    private fun initializeViews() {
        binding.filmListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filmListAdapter
        }

        binding.errorButton.setOnClickListener { viewModel.loadFilmsFromApi() }

        if (viewModel.getFilmListStatus() == Constants.POPULAR_FILM_LIST_STATUS) {
            popularFilmsButtonPressed()
        } else {
            favoritesFilmsButtonPressed()
        }

        binding.popularFilmsButton.setOnClickListener(listener)
        binding.favoritesFilmsButton.setOnClickListener(listener)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) {
            binding.filmListLoading.visibility = if(viewModel.isListEmpty() && it) View.VISIBLE else View.GONE

            if(it) binding.errorGroup.visibility = View.GONE
            if(it) binding.menuButtonsGroup.visibility = View.VISIBLE
        }

        viewModel.filmList.observe(this) {
            if (viewModel.getFilmListStatus() == Constants.POPULAR_FILM_LIST_STATUS) {
                filmListAdapter.updateList(it)

                binding.errorGroup.visibility = if (viewModel.isListEmpty()) View.VISIBLE else View.GONE
                binding.menuButtonsGroup.visibility = if (viewModel.isListEmpty()) View.GONE else View.VISIBLE
            }
        }

        viewModel.favoritesFilmList.observe(this) {
            if(viewModel.getFilmListStatus() == Constants.FAVORITES_FILM_LIST_STATUS) {
                filmListAdapter.updateList(it)
            }
        }

        viewModel.filmListStatus.observe(this) {
            when(it) {
                Constants.POPULAR_FILM_LIST_STATUS -> {
                    val filmList = viewModel.getFilmList()
                    filmList?.let { filmListAdapter.updateList(filmList) }
                }
                Constants.FAVORITES_FILM_LIST_STATUS -> {
                    val favoriteFilmList = viewModel.getFavoriteList()
                    favoriteFilmList?.let { filmListAdapter.updateList(favoriteFilmList) }
                }
            }
        }

        viewModel.favoriteStock.observe(this) {
            it?.let {
                showToast(
                    if (it.isFavorite) getString(R.string.film_add_to_favorites, it.nameRu)
                    else getString(R.string.film_delete_from_favorites, it.nameRu)
                )
            }
        }

        viewModel.toastError.observe(this) {
            showToast(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        setSearchView(menuItem)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setSearchView(menuItem: MenuItem?) {
        if(menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            SearchHelper.searchIntoList(searchView)
            setSearchViewListener(searchView)
        }
    }

    private fun setSearchViewListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotBlank()) {
                    newTasksList.clear()
                    filmListAdapter.getList().forEach { film ->
                        film.nameRu?.let { name ->
                            if (name.contains(newText)) {
                                newTasksList.add(film)
                            }
                        }
                    }
                    filmListAdapter.updateList(newTasksList)
                } else {
                    newTasksList.clear()
                    val oldList = when(viewModel.filmListStatus.value) {
                        Constants.POPULAR_FILM_LIST_STATUS -> viewModel.getFilmList()
                        Constants.FAVORITES_FILM_LIST_STATUS -> viewModel.getFavoriteList()
                        else -> viewModel.getFilmList()
                    }
                    oldList?.let {
                        newTasksList.addAll(oldList)
                        filmListAdapter.updateList(newTasksList)
                    }
                }
                return true
            }
        })
    }

    override fun onFilmInfoClick(nameId: String) {
        startActivity(Intent(this, FilmActivity::class.java).putExtra(Constants.EXTRA_FILM_ID, nameId))
    }

    override fun onFavoriteClick(nameId: String) {
        viewModel.updateFilmFavoriteStatus(nameId)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val listener = OnClickListener { view ->
        when(view.id) {
            R.id.popularFilmsButton -> {
                popularFilmsButtonPressed()
                viewModel.setFilmListStatus(Constants.POPULAR_FILM_LIST_STATUS)
            }
            R.id.favoritesFilmsButton -> {
                favoritesFilmsButtonPressed()
                viewModel.setFilmListStatus(Constants.FAVORITES_FILM_LIST_STATUS)
            }
        }
    }

    private fun popularFilmsButtonPressed() {
        with(binding) {
            popularFilmsButton.setTextColor(Color.WHITE)
            favoritesFilmsButton.setTextColor(getColor(R.color.blue))
            popularFilmsButton.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.blue)
            favoritesFilmsButton.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_blue)
        }
    }

    private fun favoritesFilmsButtonPressed() {
        with(binding) {
            popularFilmsButton.setTextColor(getColor(R.color.blue))
            favoritesFilmsButton.setTextColor(Color.WHITE)
            favoritesFilmsButton.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.blue)
            popularFilmsButton.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, R.color.light_blue)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}