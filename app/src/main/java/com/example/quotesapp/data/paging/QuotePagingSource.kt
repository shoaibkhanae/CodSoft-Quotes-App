package com.example.quotesapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.quotesapp.data.api.QuoteApiService
import com.example.quotesapp.data.model.Result
import retrofit2.HttpException
import java.io.IOException

class QuotePagingSource(private val quoteApiService: QuoteApiService)
    : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        // Most recently accessed index in the list is called anchor position
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val position = params.key ?: 1
            val response = quoteApiService.getQuotes(position)
            LoadResult.Page(
                data = response.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.totalPages) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}