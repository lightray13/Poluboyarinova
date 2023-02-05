package com.test.filmlist.ui.filmList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.filmlist.data.local.database.FilmListEntity
import com.test.filmlist.data.repository.filmList.FilmListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.test.filmlist.api.Result
import com.test.filmlist.util.Constants

@HiltViewModel
class FilmListViewModel @Inject constructor(private val repository: FilmListRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    private  val _filmListStatus = MutableLiveData<Int>()
    val filmListStatus: LiveData<Int> = _filmListStatus

    val filmList = repository.filmList
    val favoritesFilmList = repository.filmFavoriteList

    private val _favoriteStock = MutableLiveData<FilmListEntity?>()
    val favoriteStock: LiveData<FilmListEntity?> = _favoriteStock

    fun isListEmpty(): Boolean {
        return filmList.value?.isEmpty() ?: true
    }

    fun getFilmList(): List<FilmListEntity>? {
        return filmList.value
    }

    fun getFavoriteList(): List<FilmListEntity>? {
        return favoritesFilmList.value
    }

    fun loadFilmsFromApi(type: String = "TOP_100_POPULAR_FILMS") {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            repository.loadFilmList(type)
            _isLoading.postValue(false)
        }
    }

    fun updateFilmFavoriteStatus(nameId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateFilmFavoriteStatus(nameId)
            when(result) {
                is Result.Success -> _favoriteStock.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    fun setFilmListStatus(status: Int) {
        _filmListStatus.postValue(status)
    }

    fun getFilmListStatus(): Int {
        return filmListStatus.value ?: Constants.POPULAR_FILM_LIST_STATUS
    }


}