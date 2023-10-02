package com.example.skillcinema.data.repository

import com.example.skillcinema.data.db.Film
import com.example.skillcinema.data.db.FilmDao
import com.example.skillcinema.data.db.CollectionDao
import com.example.skillcinema.data.db.FilmCollection
import com.example.skillcinema.data.db.FilmCollectionDao
import com.example.skillcinema.data.db.Collection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import com.example.skillcinema.entity.Film as FilmEntity

class DatabaseRepository(
    private val filmDao: FilmDao,
    private val collectionDao: CollectionDao,
    private val filmCollectionDao: FilmCollectionDao
) {
    suspend fun addCollection(
        collectionName: String,
    ) {
        val collection = when (collectionName) {
            "Любимое" -> {
                Collection(
                    name = collectionName,
                    filmsCount = 0,
                    mainCollection = true,
                    image = "like"
                )
            }
            "Избранное" -> {
                Collection(
                    name = collectionName,
                    filmsCount = 0,
                    mainCollection = true,
                    image = "bookmark"
                )
            }
            else -> {
                Collection(
                    name = collectionName,
                    filmsCount = 0,
                    mainCollection = false,
                    image = "bottom_icon_profile"
                )
            }
        }
        try {
            collectionDao.insert(
                collection
            )
        } catch (_: Exception) {

        }
    }

    suspend fun deleteCollection(collection: Collection) {
        val films = filmCollectionDao.findCollection(collection.name)
        for (film in films) {
            filmCollectionDao.deleteFilmFromCollection(film.filmId, collection.name)
            if (filmCollectionDao.findFilm(film.filmId).isEmpty()) {
                val tempFilm = filmDao.findFilm(film.filmId)[0]
                if (!tempFilm.isViewed) {
                    filmDao.delete(film.filmId)
                }
            }
        }
        collectionDao.delete(collection)
    }

    suspend fun addFilmToCollection(film: FilmEntity, collectionName: String) {
        val collection = collectionDao.getCollection(collectionName)
        if (collection.isEmpty())
            addCollection(collectionName)
        else collectionDao.update(
            Collection(
                name = collection[0].name,
                filmsCount = collection[0].filmsCount + 1,
                mainCollection = collection[0].mainCollection,
                image = collection[0].image
            )
        )
        try {
            filmDao.insert(
                Film(
                    filmId = film.filmId,
                    posterUrlPreview = film.posterUrlPreview,
                    rating = film.rating,
                    name = film.name,
                    genres = film.genres.joinToString { it.name },
                    isViewed = false
                )
            )
        } catch (_: Exception) {
            filmDao.update(
                Film(
                    filmId = film.filmId,
                    posterUrlPreview = film.posterUrlPreview,
                    rating = film.rating,
                    name = film.name,
                    genres = film.genres.joinToString { it.name },
                    isViewed = false
                )
            )
        }
        filmCollectionDao.insert(
            FilmCollection(
                filmId = film.filmId,
                collectionName = collectionName
            )
        )
    }

    suspend fun deleteFilmFromCollection(film: FilmEntity, collectionName: String) {
        filmCollectionDao.deleteFilmFromCollection(film.filmId, collectionName)
        if (filmCollectionDao.findFilm(film.filmId).isEmpty()) {
            val tempFilm = filmDao.findFilm(film.filmId)[0]
            if (!tempFilm.isViewed) {
                filmDao.delete(film.filmId)
            }
        }
    }

    fun getViewedFilms(viewModelScope: CoroutineScope): StateFlow<List<Film>> =
        filmDao.getViewed().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    suspend fun addViewedFilm(film: FilmEntity) {
        try {
            filmDao.insert(
                Film(
                    filmId = film.filmId,
                    posterUrlPreview = film.posterUrlPreview,
                    rating = film.rating,
                    name = film.name,
                    genres = film.genres.joinToString { it.name },
                    isViewed = true
                )
            )
        } catch (_: Exception) {
            filmDao.update(
                Film(
                    filmId = film.filmId,
                    posterUrlPreview = film.posterUrlPreview,
                    rating = film.rating,
                    name = film.name,
                    genres = film.genres.joinToString { it.name },
                    isViewed = true
                )
            )
        }

    }

    suspend fun deleteViewedFilm(film: FilmEntity) {
        if (filmCollectionDao.findFilm(film.filmId).isEmpty()) {
            filmDao.delete(film.filmId)
        } else {
            filmDao.update(
                Film(
                    filmId = film.filmId,
                    posterUrlPreview = film.posterUrlPreview,
                    rating = film.rating,
                    name = film.name,
                    genres = film.genres.joinToString { it.name },
                    isViewed = false
                )
            )
        }
    }

    fun getCollections(viewModelScope: CoroutineScope): StateFlow<List<Collection>> =
        this.collectionDao.getAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    suspend fun getCurrentCollection(
        collectionName: String
    ): StateFlow<List<Film>> {
        val filmsInCollection: MutableStateFlow<MutableList<Film>> =
            MutableStateFlow(mutableListOf())
        filmCollectionDao.getCollection(collectionName).forEach {
            filmsInCollection.value.add(filmDao.findFilm(it.filmId)[0])
        }
        return filmsInCollection.asStateFlow()
    }

    suspend fun findFilmInCollection(filmId: Int, collectionName: String): Boolean {
        val filmList = filmCollectionDao.findFilm(filmId)
        for (film in filmList) {
            if (film.collectionName == collectionName) {
                return true
            }
        }
        return false
    }

    suspend fun findViewedFilm(filmId: Int): Boolean {
        val film = filmDao.findFilm(filmId)
        if (film.isEmpty())
            return false
        return film[0].isViewed
    }
}