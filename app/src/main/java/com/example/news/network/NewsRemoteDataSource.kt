package com.example.news.network

import com.example.news.Result
import com.example.news.domain.News

class NewsRemoteDataSource(private val newsService: GoogleNewsApiService) {
   lateinit var data: Result<List<News>>
    suspend fun getNewsListFromWeb(queryText: String): Result<List<News>> {

        return if (isInternetAvailable()) {
            val newsListContainer = newsService.getEverythingNews(queryText)

            newsListContainer.let {
                if (it.isSuccessful) {
                    val result = Result.Success(it.body()!!.asDomainModel())
                    data=result
                    return result
                } else {
                    Result.Error(Exception(it.errorBody().toString()))
                }
            }
        } else {
            return Result.Error(InternetMissingException())
        }
    }


}

class InternetMissingException : Exception()