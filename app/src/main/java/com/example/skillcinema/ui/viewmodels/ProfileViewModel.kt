package com.example.skillcinema.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.db.Film
import com.example.skillcinema.data.db.Collection
import com.example.skillcinema.data.db.FilmDao
import com.example.skillcinema.entity.Film as FilmEntity
import com.example.skillcinema.entity.Collection as CollectionEntity
import com.example.skillcinema.data.repository.DatabaseRepository
import com.example.skillcinema.entity.FilmById
import com.example.skillcinema.entity.FilmState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {
    val viewed = databaseRepository.getViewedFilms(viewModelScope)

    val collections = databaseRepository.getCollections(viewModelScope)

    private val _currentFilm = MutableStateFlow(
        FilmState(
            isViewed = false,
            favourite = false,
            like = false
        )
    )
    val currentFilm = _currentFilm.asStateFlow()

    private val _currentCollection =
        MutableStateFlow<List<Film>>(emptyList())
    val currentCollection = _currentCollection.asStateFlow()

    private val _history = MutableStateFlow<List<Film>>(emptyList())
    val history = _history.asStateFlow()

    val collectionList = mutableListOf<Collection>()

    fun addCollection(collectionName: String) {
        viewModelScope.launch {
            databaseRepository.addCollection(collectionName)
        }
    }

    fun addFilmToCollection(film: FilmEntity, collection: Collection) {
        viewModelScope.launch {
            databaseRepository.addFilmToCollection(film, collectionName = collection.name)
        }
    }

    suspend fun addFilmToLikes(film: FilmEntity) {
        databaseRepository.addFilmToCollection(film, "Любимое")
    }

    suspend fun addFilmToFavourites(film: FilmEntity) {
        databaseRepository.addFilmToCollection(film, "Избранное")
    }

    suspend fun addFilmToIsViewed(film: FilmEntity) {
        databaseRepository.addViewedFilm(film)
    }

    suspend fun deleteViewedFilm(film: FilmEntity) {
        databaseRepository.deleteViewedFilm(film)
    }

    suspend fun deleteFilmFromCollection(film: FilmEntity, collectionName: String) {
        databaseRepository.deleteFilmFromCollection(film, collectionName)
    }

    suspend fun deleteCollection(collection: Collection) {
        databaseRepository.deleteCollection(collection)
    }

    fun setCurrentCollection(collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentCollection.value =
                databaseRepository.getCurrentCollection(collectionName).value
        }
    }

    suspend fun checkCurrentFilm(filmById: FilmById) {
        var like = false
        var favourite = false
        var isViewed = false
        if (databaseRepository.findFilmInCollection(
                filmId = filmById.kinopoiskId,
                collectionName = "Любимое"
            )
        ) like = true
        if (databaseRepository.findFilmInCollection(
                filmId = filmById.kinopoiskId,
                collectionName = "Избранное"
            )
        ) favourite = true
        if (databaseRepository.findViewedFilm(filmId = filmById.kinopoiskId)) isViewed = true

        _currentFilm.value = FilmState(isViewed = isViewed, like = like, favourite = favourite)
    }
}