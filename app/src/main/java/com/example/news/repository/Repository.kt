package com.example.news.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.news.api.GoogleNewsApi
import com.example.news.api.parseNewsJsonResult
import com.example.news.models.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar

enum class FetchingState {
    IDLE, DOWNLOADING_NEWS, CHECKING_UP_COMMENTS
}

class Repository {
    val news = MutableLiveData<List<News>>()
    val dataFetchingState = MutableLiveData<FetchingState>()

    init {
        dataFetchingState.value = FetchingState.IDLE
    }

    suspend fun fetchNews(queryText:String) {
        withContext(Dispatchers.IO) {
            try {
                val updatedNews = fetchNewsFromGoogle(queryText)

                news.postValue(updatedNews)
                dataFetchingState.postValue(FetchingState.IDLE)
            } catch (exception: Exception) {
                Timber.log(Log.ERROR, exception.message)
                dataFetchingState.postValue(FetchingState.IDLE)

                throw exception
            }
        }
    }

    private suspend fun fetchNewsFromGoogle(queryText:String ): ArrayList<News> {
        dataFetchingState.postValue(FetchingState.DOWNLOADING_NEWS)

        val returnedNews = JSONObject(GoogleNewsApi.retrofitService.getEverythingNews(queryText))

        return parseNewsJsonResult(returnedNews) as ArrayList<News>
    }


    private fun getFormattedCurrentDateTime(): String {
        val isDatePattern = "yyyy-MM-dd"
        val format = SimpleDateFormat(isDatePattern)

        return format.format(Calendar.getInstance().time)
    }
}