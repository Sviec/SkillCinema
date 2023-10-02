package com.example.skillcinema.data.api

import com.example.skillcinema.data.entity.filters.FilmsByFiltersDao
import com.example.skillcinema.data.entity.FilmsDao
import com.example.skillcinema.data.entity.PremieresDao
import com.example.skillcinema.data.entity.SerialsDao
import com.example.skillcinema.data.entity.filmById.*
import com.example.skillcinema.data.entity.filters.FiltersGenresCountriesDao
import com.example.skillcinema.data.entity.filters.StaffByNameDao
import com.example.skillcinema.data.entity.staffById.StaffByIdDao
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val kinopoiskApi: KinopoiskApi = retrofit.create(KinopoiskApi::class.java)
}

interface KinopoiskApi {
    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/top?type=TOP_250_BEST_FILMS")
    suspend fun topList(@Query("page") page: Int): FilmsDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/premieres")
    suspend fun premieresList(@Query("year") year: Int, @Query("month") month: String): PremieresDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun popularList(@Query("page") page: Int): FilmsDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films?type=TV_SERIES")
    suspend fun seriesList(@Query("page") page: Int): SerialsDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/filters")
    suspend fun genresAndCountriesList(): FiltersGenresCountriesDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films")
    suspend fun filmByFiltersList(
        @Query("countries") countries: String,
        @Query("genres") genres: String,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("imdbId") imdbId: String?,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): FilmsByFiltersDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v1/persons")
    suspend fun staffByName(@Query("name") name: String): StaffByNameDao


    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}")
    suspend fun filmById(@Path("id") id: Int): FilmByIdDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v1/staff")
    suspend fun filmByIdStaff(@Query("filmId") id: Int): List<FilmByIdStaffDao>

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun filmByIdSimilars(@Path("id") id: Int): SimilarsDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}/images")
    suspend fun filmByIdImages(@Path("id") id: Int, @Query("type") type: String = "STILL"): ImagesDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v1/staff/{id}")
    suspend fun staffById(@Path("id") id: Int): StaffByIdDao

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun filmByIdSeasons(@Path("id") id: Int): SeasonsDao



    private companion object {
        private const val api_key = "9869d0be-c3e1-4015-bcac-621a1190fc89"
    }
}