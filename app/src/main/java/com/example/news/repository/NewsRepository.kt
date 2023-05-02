package com.example.news.repository

import androidx.lifecycle.LiveData
import com.example.news.Result
import com.example.news.database.NewsLocalDataSource
import com.example.news.domain.News
import com.example.news.network.NewsRemoteDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NewsRepository private constructor(
    private val newsLocalDataSource: NewsLocalDataSource,

    private val newsRemoteDataSource: NewsRemoteDataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null

        fun getRepository(
            newsLocalDataSource: NewsLocalDataSource,
            newsRemoteDataSource: NewsRemoteDataSource
        ): NewsRepository {
            return INSTANCE ?: synchronized(this) {
                NewsRepository(newsLocalDataSource, newsRemoteDataSource).also {
                    INSTANCE = it
                }
            }
        }
    }


    fun observeSavedNewsList(): LiveData<Result<List<News>>> {
        return newsLocalDataSource.observeNewsList()
    }

    fun observeSavedNewsList(url: String): LiveData<Result<News>> {
        return newsLocalDataSource.observeNews(url)
    }


    suspend fun getSavedNews(url: String, forceUpdate: Boolean = false): Result<News> {
        return newsLocalDataSource.getNews(url)
    }

    suspend fun saveNews(news: News) {
        coroutineScope {
            launch { newsLocalDataSource.saveNews(news) }
        }
    }

    suspend fun deleteNews(url: String) {
        coroutineScope {
            launch { newsLocalDataSource.deleteNews(url) }
        }
    }


     fun observeNewsListFromWeb():  LiveData<Result<List<News>>>  {

        return newsRemoteDataSource.observeNewsList()

    }

    suspend fun updateNewsListFromWeb(queryText: String) {
        newsRemoteDataSource.getNewsListFromWeb(queryText)
    }


}
