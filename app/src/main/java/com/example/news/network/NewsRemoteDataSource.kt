package com.example.news.network

import com.example.news.Result
import com.example.news.domain.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRemoteDataSource(private val newsService: GoogleNewsApiService) {
    lateinit var data: Result<List<News>>
    suspend fun getNewsListFromWeb(queryText: String) {
        withContext(Dispatchers.IO) {
            if (isInternetAvailable()) {
                val newsListContainer = newsService.getEverythingNews(queryText)

                newsListContainer.let {
                    data = if (it.isSuccessful) {
                        val result = Result.Success(it.body()!!.asDomainModel())
                        result

                    } else {
                        Result.Error(Exception(it.errorBody().toString()))
                    }
                }
            } else {
                data = Result.Error(InternetMissingException())
            }
        }
    }


}

class InternetMissingException : Exception()