package com.test.filmlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.filmlist.R
import com.test.filmlist.data.local.database.FilmListEntity
import com.test.filmlist.databinding.ItemFilmBinding
import com.test.filmlist.util.ImageLoader

interface OnItemClickCallback {
    fun onFilmInfoClick(filmId: String)
    fun onFavoriteClick(filmId: String)
}

class FilmListAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder>() {
    private val filmList = mutableListOf<FilmListEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFilmBinding.inflate(inflater, parent, false)

        return FilmListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmListViewHolder, position: Int) {
        val film = filmList[position]
        with(holder.binding) {
            filmItemNameTextView.text = film.nameRu ?: ""
            val context = holder.itemView.context
            filmItemDescriptionTextView.text = context.getString(R.string.film_description, film.genresList, film.year)
            ImageLoader.loadImage(filmItemImageView, film.posterUrlPreview ?: "")
            filmItemFavoriteImageView.visibility = if (film.isFavorite) View.VISIBLE else View.GONE
            root.setOnClickListener {
                onItemClickCallback.onFilmInfoClick(film.nameRu)
            }
            root.setOnLongClickListener {
                onItemClickCallback.onFavoriteClick(film.nameRu)
                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    fun updateList(list: List<FilmListEntity>) {
        this.filmList.clear()
        this.filmList.addAll(list)
        notifyDataSetChanged()
    }

    fun getList(): List<FilmListEntity> {
        return filmList
    }

    class FilmListViewHolder(
        val binding: ItemFilmBinding
        ): RecyclerView.ViewHolder(binding.root)
}