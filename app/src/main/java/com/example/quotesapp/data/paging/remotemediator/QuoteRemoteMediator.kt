package com.example.quotesapp.data.paging.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.quotesapp.data.api.QuoteApiService
import com.example.quotesapp.data.db.QuoteDatabase
import com.example.quotesapp.data.model.RemoteKeys
import com.example.quotesapp.data.model.Result
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
    private val service: QuoteApiService,
    private val database: QuoteDatabase
): RemoteMediator<Int, Result>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1,TimeUnit.HOURS)

        return if(System.currentTimeMillis() - (database.getRemoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        val page: Int = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosesToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyFirstTime(state)
                val prevKey = remoteKeys?.previousKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val response = service.getQuotes(page)
            val quotes = response.results
            val endOfPaginationReached = quotes.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getRemoteKeysDao().clearRemoteKeys()
                    database.getQuotesDao().clearAllQuotes()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = quotes.map {
                    RemoteKeys(quoteId = it.id, previousKey = prevKey, currentPage = page, nextKey = nextKey)
                }

                database.getRemoteKeysDao().insertAll(remoteKeys)
                database.getQuotesDao().insertAll(quotes.onEachIndexed { _, quote -> quote.page = page })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getRemoteKeyClosesToCurrentPosition(state: PagingState<Int, Result>): RemoteKeys? {
        return state.anchorPosition?.let { id ->
            database.getRemoteKeysDao().getRemoteKeysByQuoteId(id.toString())
        }
    }

    private suspend fun getRemoteKeyFirstTime(state: PagingState<Int, Result>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { quotes ->
            database.getRemoteKeysDao().getRemoteKeysByQuoteId(quotes.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Result>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { quotes ->
            database.getRemoteKeysDao().getRemoteKeysByQuoteId(quotes.id)
        }
    }

}