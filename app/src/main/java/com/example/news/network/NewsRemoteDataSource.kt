package com.example.news.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news.Result
import com.example.news.domain.News
import org.koin.java.KoinJavaComponent.inject

class NewsRemoteDataSource(private val newsService: GoogleNewsApiService) {

    private val observableNewsList = MutableLiveData<Result<List<News>>>()

    fun observeNewsList(): LiveData<Result<List<News>>> {
        return observableNewsList
    }

    suspend fun getNewsListFromWeb(queryText: String): Result<List<News>> {
        val newsListContainer = newsService.getEverythingNews(queryText)

        newsListContainer.apply {
            return if (isSuccessful) {
                val result = Result.Success(body()!!.asDomainModel())
                observableNewsList.value = result
                return result
            } else {
                Result.Error(Exception(errorBody().toString()))
            }
        }
    }

}