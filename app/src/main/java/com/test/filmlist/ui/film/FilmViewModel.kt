package com.test.filmlist.ui.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.filmlist.data.repository.film.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(private val repository: FilmRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun filmById(nameId: String) = repository.filmById(nameId)

    fun loadFilmFromApi(nameId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            repository.loadFilmById(nameId)
            _isLoading.postValue(false)
        }
    }
}