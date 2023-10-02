package com.example.skillcinema.data.repository

import android.util.Log
import com.example.skillcinema.data.api.RetrofitInstance
import com.example.skillcinema.data.entity.filters.FiltersParams
import com.example.skillcinema.entity.*

class CinemaRepository {
    suspend fun getPremieres(year: Int, month: String): List<Film> {
        return RetrofitInstance.kinopoiskApi.premieresList(year, month).items
    }
    suspend fun getSerials(page: Int): List<Film> {
        return RetrofitInstance.kinopoiskApi.seriesList(page).items
    }
    suspend fun getTop(page: Int): List<Film> {
        return RetrofitInstance.kinopoiskApi.topList(page).films
    }
    suspend fun getPopular(page: Int): List<Film> {
        return RetrofitInstance.kinopoiskApi.popularList(page).films
    }
    suspend fun getGenresAndCountries(): GenresAndCountries {
        val items = RetrofitInstance.kinopoiskApi.genresAndCountriesList()
        return object: GenresAndCountries {
            override val genres: List<CountryGenre> = items.genres
            override val countries: List<CountryGenre> = items.countries
        }
    }
    suspend fun getFilmsByFilters(filters: FiltersParams, page: Int) : List<Film> {
        Log.d("SearchTest", "CinemaRepository: getting films by filters")
        return RetrofitInstance.kinopoiskApi.filmByFiltersList(
            countries = filters.countries,
            genres = filters.genres,
            order = filters.order,
            type = filters.type,
            ratingFrom = filters.ratingFrom,
            ratingTo = filters.ratingTo,
            yearFrom = filters.yearFrom,
            yearTo = filters.yearTo,
            imdbId = filters.imdbId,
            keyword = filters.keyword,
            page = page
        ).items
    }
    suspend fun getStaffByName(name: String) : List<Staff> {
        return RetrofitInstance.kinopoiskApi.staffByName(name).items
    }
    suspend fun getFilmById(id: Int) : FilmById {
        return RetrofitInstance.kinopoiskApi.filmById(id)
    }
    suspend fun getImages(id: Int) : List<Image> {
        return RetrofitInstance.kinopoiskApi.filmByIdImages(id).items
    }
    suspend fun getSimilars(id: Int) : List<Film> {
        return RetrofitInstance.kinopoiskApi.filmByIdSimilars(id).items
    }
    suspend fun getStaff(id: Int) : List<Staff> {
        return RetrofitInstance.kinopoiskApi.filmByIdStaff(id)
    }
    suspend fun getStaffById(id: Int) : StaffById {
        return RetrofitInstance.kinopoiskApi.staffById(id)
    }
    suspend fun getSeasons(id: Int) : List<Season> {
        return RetrofitInstance.kinopoiskApi.filmByIdSeasons(id).items
    }
}