package com.example.skillcinema.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.entity.Film

class FilmsPagingSource(
    private val filmList: List<Film>
) : PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Film> {
        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(20)

        val items = filmList
        return if (items.isNotEmpty()) {
            val nextKey = if (items.size < pageSize) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            PagingSource.LoadResult.Page(items, prevKey, nextKey)
        } else {
            PagingSource.LoadResult.Error(Exception())
        }
    }
}