package com.example.news.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.news.Result
import com.example.news.domain.News
import com.example.news.network.NewsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/*
enum class FetchingState {
    IDLE, DOWNLOADING_NEWS
}

class NewsRepository(private val database: NewsDatabase) {

    val news: MutableLiveData<List<News>> = database.newsDao.getNews().map() {
        it.asDomainModel()
    } as MutableLiveData<List<News>>


    val dataFetchingState = MutableLiveData<FetchingState>()

    init {
        dataFetchingState.value = FetchingState.IDLE
    }

    suspend fun refreshNews(searchQuery: String = "Brasil") {
        withContext(Dispatchers.IO) {
            val newList = NewsNetwork.newsService.getEverythingNews(searchQuery)
            //   database.newsDao.insertAll(newList.asDatabaseModel())
        }
    }


    suspend fun fetchNewsFromWeb(queryText: String) {
        withContext(Dispatchers.IO) {
            try {
                val newsFromWeb = fetchNewsFromGoogleService(queryText)
                val newsWithCompleteInformation = newsFromWeb.filter { it.hasCompleteInformation() }

                news.postValue(newsWithCompleteInformation)
                dataFetchingState.postValue(FetchingState.IDLE)
            } catch (exception: Exception) {
                Timber.log(Log.ERROR, exception.message)
                dataFetchingState.postValue(FetchingState.IDLE)

                throw exception
            }
        }
    }

    private suspend fun fetchNewsFromGoogleService(queryText: String): ArrayList<News> {
        dataFetchingState.postValue(FetchingState.DOWNLOADING_NEWS)

        val returnedNewsNetworkContainer =
            NewsNetwork.newsService.getEverythingNews(queryText).body()


        return returnedNewsNetworkContainer!!.asDomainModel() as ArrayList<News>
    }
}*/


class NewsRepository private constructor(application: Application) {

    private val newsRemoteDataSource = NewsRemoteDataSource

    //  private val tasksLocalDataSource:NewsLocalLocalDataSource
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null

        fun getRepository(app: Application): NewsRepository {
            return INSTANCE ?: NewsRepository(app).also {
                INSTANCE = it

            }
        }
    }



    //  val database = NewsDatabase.getInstance(application)

    //    tasksLocalDataSource = NewsLocalLocalDataSource(database.newsDao())


    /*   fun observeSavedNewsList(): LiveData<Result<List<News>>> {
         return newsDataSource.observeNewsList()
     }



     fun observeSavedNewsList(url: String): LiveData<Result<News>> {
         return newsDataSource.observeNews(url)
     }


     suspend fun getSavedNews(url: String, forceUpdate: Boolean = false): Result<News> {
         return newsDataSource.getNews(url)
     }

     suspend fun saveNews(news: News) {
         coroutineScope {
             launch { newsDataSource.saveNews(news) }
         }
     }

     suspend fun deleteNews(url: String) {
         coroutineScope {
             launch { newsDataSource.deleteNews(url) }
         }
     }

     private suspend fun getNewsWithUrl(url: String): Result<News> {
         return newsDataSource.getNews(url)
     }*/
    fun observeNewsListFromWeb(): LiveData<Result<List<News>>> {
        return newsRemoteDataSource.observeNewsList()
    }

    suspend fun updateNewsListFromWeb(queryText: String) {
        newsRemoteDataSource.getNewsListFromWeb(queryText)
    }

}
