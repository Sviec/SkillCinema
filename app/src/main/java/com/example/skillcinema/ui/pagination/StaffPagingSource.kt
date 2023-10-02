package com.example.skillcinema.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.entity.Staff

class StaffPagingSource(
    private val staffList: List<Staff>
) : PagingSource<Int, Staff>() {
    override fun getRefreshKey(state: PagingState<Int, Staff>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Staff> {
        val page = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(20)

        val items = staffList
        return if (items.isNotEmpty()) {
            val nextKey = if (items.size < pageSize) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(items, prevKey, nextKey)
        } else {
            LoadResult.Error(Exception())
        }
    }
}