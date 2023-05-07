package com.example.news.network

import com.example.news.Result
import com.example.news.domain.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRemoteDataSource(private val newsService: GoogleNewsApiService) {
    lateinit var lastSearchQuery: String
    suspend fun getNewsByQuery(queryText: String): Result<List<News>> {
        lastSearchQuery = queryText
        return getNewsFromSearch(queryText)
    }

    suspend fun refreshCurrentSearch(): Result<List<News>> {
        return getNewsFromSearch(lastSearchQuery)
    }

    private suspend fun getNewsFromSearch(queryText: String): Result<List<News>> {
       return withContext(Dispatchers.IO) {
            if (isInternetAvailable()) {
                val newsListContainer = newsService.getEverythingNews(queryText)

                newsListContainer.let {
                    if (it.isSuccessful) {
                        Result.Success(it.body()!!.asDomainModel())
                    } else {
                        Result.Error(Exception(it.errorBody().toString()))
                    }
                }
            } else {
                Result.Error(InternetMissingException())
            }
        }
    }
}


class InternetMissingException : Exception()