package com.example.news.repository

import com.example.news.database.NewsLocalDataSource
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.news.Result
import com.example.news.database.NewsDatabase
import com.example.news.domain.News
import com.example.news.network.NewsRemoteDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NewsRepository private constructor(application: Application) {

    private val newsRemoteDataSource = NewsRemoteDataSource
    private val newsLocalDataSource: NewsLocalDataSource

    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null

        fun getRepository(app: Application): NewsRepository {
            return INSTANCE ?: synchronized(this) {
                NewsRepository(app).also {
                    INSTANCE = it
                }
            }
        }
    }

    init {
        val database = Room.databaseBuilder(application.applicationContext,
            NewsDatabase::class.java, "news.db")
            .build()

        newsLocalDataSource = NewsLocalDataSource(database.newsDao)
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

     private suspend fun getNewsWithUrl(url: String): Result<News> {
         return newsLocalDataSource.getNews(url)
     }
    fun observeNewsListFromWeb(): LiveData<Result<List<News>>> {
        return newsRemoteDataSource.observeNewsList()
    }

    suspend fun updateNewsListFromWeb(queryText: String) {
        newsRemoteDataSource.getNewsListFromWeb(queryText)
    }

}
