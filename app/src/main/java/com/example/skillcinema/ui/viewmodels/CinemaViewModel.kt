package com.example.skillcinema.ui.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.entity.CountryDao
import com.example.skillcinema.data.entity.GenreDao
import com.example.skillcinema.data.entity.filters.FiltersParams
import com.example.skillcinema.data.entity.staffById.StaffByIdFilmDao
import com.example.skillcinema.data.repository.CinemaRepository
import com.example.skillcinema.entity.*
import com.example.skillcinema.ui.pagination.FilmsByCategoryPagingSource
import com.example.skillcinema.ui.pagination.FilmsPagingSource
import com.example.skillcinema.ui.pagination.StaffPagingSource
import com.example.skillcinema.utils.Categories
import com.example.skillcinema.utils.StateLoading
import com.example.skillcinema.utils.convertToMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CinemaViewModel : ViewModel() {

    private val repository = CinemaRepository()
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _currentCategory = MutableStateFlow(Category("", emptyList()))
    val currentCategory = _currentCategory.asStateFlow()
//
//    val currentCategory: Flow<PagingData<Film>> = Pager(
//        config = PagingConfig(pageSize = 20),
//        pagingSourceFactory = {
//            FilmsByCategoryPagingSource(category = _currentCategory.value)
//        }
//    ).flow.cachedIn(viewModelScope)

    private val _loadCategoryState = MutableStateFlow<StateLoading>(StateLoading.Default)
    val loadCategoryState = _loadCategoryState.asStateFlow()

    private val _currentStaff = MutableStateFlow<StaffById>(object : StaffById {
        override val personId: Int = -1
        override val name: String = ""
        override val profession: String = ""
        override val posterUrl: String? = null
        override val films: List<StaffByIdFilmDao> = emptyList()
    })
    val currentStaff = _currentStaff.asStateFlow()

    private val _currentFilm = MutableStateFlow<FilmById>(object : FilmById {
        override val kinopoiskId: Int = -1
        override val name: String = ""
        override val posterUrl: String = ""
        override val ratingKinopoisk: Float = 0F
        override val filmLength: Int = 1
        override val description: String = ""
        override val shortDescription: String = ""
        override val countries: List<CountryDao> = emptyList()
        override val genres: List<GenreDao> = emptyList()
        override val year: Int = 0
        override val serial: Boolean = true
    })
    val currentFilm = _currentFilm.asStateFlow()

    private val _currentSeasons = MutableStateFlow<List<Season>>(emptyList())
    val currentSeasons = _currentSeasons.asStateFlow()

    private val _currentFilmActors = MutableStateFlow<List<Staff>>(emptyList())
    val currentFilmActors = _currentFilmActors.asStateFlow()

//    val currentFilmActors: Flow<PagingData<Staff>> = Pager(
//        config = PagingConfig(pageSize = 20),
//        pagingSourceFactory = {
//            StaffPagingSource(staffList = _currentFilmActors.value)
//        }
//    ).flow.cachedIn(viewModelScope)

    private val _currentFilmWorkers = MutableStateFlow<List<Staff>>(emptyList())
    val currentFilmWorkers = _currentFilmWorkers.asStateFlow()

//    val currentFilmWorkers: Flow<PagingData<Staff>> = Pager(
//        config = PagingConfig(pageSize = 20),
//        pagingSourceFactory = {
//            StaffPagingSource(staffList = _currentFilmWorkers.value)
//        }
//    ).flow.cachedIn(viewModelScope)

    private val _currentFilmGallery = MutableStateFlow<List<Image>>(emptyList())
    val currentFilmGallery = _currentFilmGallery.asStateFlow()

    private val _currentFilmSimilar = MutableStateFlow<List<Film>>(emptyList())
    val currentFilmSimilar = _currentFilmSimilar.asStateFlow()

//    val currentFilmSimilar: Flow<PagingData<Film>> = Pager(
//        config = PagingConfig(pageSize = 20),
//        pagingSourceFactory = {
//            FilmsPagingSource(filmList = _currentFilmSimilar.value)
//        }
//    ).flow.cachedIn(viewModelScope)

    init {
        getFilmsCategories()
    }

    fun getFilmsCategories() {
        try {
            val calendar = Calendar.getInstance()
            viewModelScope.launch(Dispatchers.IO) {
                _loadCategoryState.value = StateLoading.Loading
                val year: Int = calendar.get(Calendar.YEAR)
                val month: String = convertToMonth(calendar.get(Calendar.MONTH) + 1).name
                val allCategoriesGenres = repository.getGenresAndCountries()
                val randomCountries = listOf(
                    allCategoriesGenres.countries.random(),
                    allCategoriesGenres.countries.random()
                )
                val randomGenres = listOf(
                    allCategoriesGenres.genres.random(),
                    allCategoriesGenres.genres.random(),
                )
                val categoriesList = listOf(
                    Category(
                        category = Categories.PREMIERES.category,
                        filmList = repository.getPremieres(year, month)
                    ),
                    Category(
                        category = Categories.POPULAR.category,
                        filmList = repository.getPopular(1)
                    ),
                    Category(
                        category = "${randomGenres[0].name} ${randomCountries[0].name}",
                        filmList = repository.getFilmsByFilters(
                            FiltersParams(
                                countries = randomCountries[0].id.toString(),
                                genres = randomGenres[0].id.toString()
                            ),
                            page = 1
                        )
                    ),
                    Category(
                        category = "${randomGenres[1].name} ${randomCountries[1].name}",
                        filmList = repository.getFilmsByFilters(
                            FiltersParams(
                                countries = randomCountries[1].id.toString(),
                                genres = randomGenres[1].id.toString()
                            ),
                            page = 1
                        )
                    ),
                    Category(
                        category = Categories.TOP.category,
                        filmList = repository.getTop(1)
                    ),
                    Category(
                        category = Categories.SERIALS.category,
                        filmList = repository.getSerials(1)
                    )
                )
                _loadCategoryState.value = StateLoading.Success
                _categories.value = categoriesList
            }
        } catch (e: Throwable) {
            _loadCategoryState.value = StateLoading.Error(e.message.toString())
        }
    }

    private fun getFilmById(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentFilm.value = repository.getFilmById(filmId)
            if (_currentFilm.value.serial) {
                _currentSeasons.value = repository.getSeasons(filmId)
            }
            val filmStaff = repository.getStaff(filmId)
            _currentFilmActors.value = filmStaff.filter {
                it.professionKey == "ACTOR"
            }
            _currentFilmWorkers.value = filmStaff.filter {
                it.professionKey != "ACTOR"
            }
            _currentFilmSimilar.value = repository.getSimilars(filmId)
            _currentFilmGallery.value = repository.getImages(filmId)
        }
    }

    fun setCurrentFilm(id: Int) {
        getFilmById(id)
    }

    fun setCurrentStaff(id: Int) {
        viewModelScope.launch {
            _currentStaff.value = repository.getStaffById(id)
        }
    }

    fun setCurrentCategory(category: Category) {
        _currentCategory.value = category
    }
}

