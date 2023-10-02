package com.example.skillcinema.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.entity.filters.FiltersParams
import com.example.skillcinema.data.repository.CinemaRepository
import com.example.skillcinema.entity.CountryGenre
import com.example.skillcinema.entity.Film
import com.example.skillcinema.ui.pagination.FilmsByFilterPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val cinemaRepository = CinemaRepository()
    private var filters = FiltersParams()
    private val _isFilterChanged = MutableStateFlow(false)
    val isFilterChanged = _isFilterChanged.asStateFlow()


    private val _countries = MutableStateFlow<List<CountryGenre>>(emptyList())
    val countries = _countries.asStateFlow()

    private val _genres = MutableStateFlow<List<CountryGenre>>(emptyList())
    val genres = _genres.asStateFlow()

    val films: Flow<PagingData<Film>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            FilmsByFilterPagingSource(
                filters = filters,
                repository = cinemaRepository
            )
        }
    ).flow.cachedIn(viewModelScope)

    init {
        getCountriesOrGenres()
    }

    fun getFilters(): FiltersParams {
        return filters
    }

    fun updateFilters(filterFilm: FiltersParams) {
        _isFilterChanged.value = false
        filters = filterFilm
        _isFilterChanged.value = true
    }

    private fun getCountriesOrGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            _countries.value =
                cinemaRepository.getGenresAndCountries().countries.sortedBy { it.name }
            _genres.value =
                cinemaRepository.getGenresAndCountries().genres.sortedBy { it.name }
        }
    }
}