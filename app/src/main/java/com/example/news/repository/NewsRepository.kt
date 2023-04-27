package com.example.news.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.news.database.NewsDatabase
import com.example.news.database.asDomainModel
import com.example.news.domain.News
import com.example.news.network.NewsNetwork
import com.example.news.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

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
}