package com.example.skillcinema.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.entity.Category
import com.example.skillcinema.entity.Film

class FilmsByCategoryPagingSource(
    private val category: Category
) : PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(20)

        val items = category.filmList
        return if (items.isNotEmpty()) {
            val nextKey = if (items.size < pageSize) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(items, prevKey, nextKey)
        } else {
            LoadResult.Error(Exception())
        }
    }
}