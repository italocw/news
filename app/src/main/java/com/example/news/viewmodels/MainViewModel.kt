package com.example.news.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.database.getDatabase
import com.example.news.domain.News
import com.example.news.network.isInternetAvailable
import com.example.news.repository.FetchingState
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.launch

enum class NewsListScreenStatus {
    LOADING, SUCCESS, CONNECTION_PROBLEM, EMPTY_LIST, ERROR
}

const val DEFAULT_QUERY_TEXT = "Brasil"

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val newsRepository = NewsRepository(getDatabase(application))

    private val _navigateToNews = MutableLiveData<News?>()
    val navigateToNews
        get() = _navigateToNews

    private val _news: LiveData<List<News>> =
        newsRepository.news
    val news
        get() = _news

    private val _dataFetchingState: LiveData<FetchingState> = newsRepository.dataFetchingState
    val dataFetchingState
        get() = _dataFetchingState


    private val _screenStatus = MutableLiveData<NewsListScreenStatus>()
    val screenStatus
        get() = _screenStatus


    private val _queryText = MutableLiveData<String>()
    val queryText
        get() = _queryText


    init {
        fetchOnlineDataIfHasInternetConnection()
    }

    private fun fetchOnlineDataIfHasInternetConnection(queryText: String? = DEFAULT_QUERY_TEXT) {
        viewModelScope.launch {
            _screenStatus.value = NewsListScreenStatus.LOADING

            try {
                if (isInternetAvailable()) {
                    newsRepository.fetchNewsFromWeb(queryText ?: DEFAULT_QUERY_TEXT)

                    if (news.value!!.isEmpty()) {
                        _screenStatus.value = NewsListScreenStatus.EMPTY_LIST
                    } else {
                        _screenStatus.value = NewsListScreenStatus.SUCCESS
                    }

                } else {
                    _screenStatus.value = NewsListScreenStatus.CONNECTION_PROBLEM
                }

            } catch (exception: java.lang.Exception) {
                _screenStatus.value = NewsListScreenStatus.ERROR
            }
        }
    }

    fun onNewsClicked(news: News) {
        _navigateToNews.value = news
    }


    fun onNewsTextQuerySubmit() {
        fetchOnlineDataIfHasInternetConnection(_queryText.value)
    }


    fun onNewsNavigated() {
        _navigateToNews.value = null
    }

    fun refreshNews() {
        if (screenStatus.value != NewsListScreenStatus.LOADING) {
            fetchOnlineDataIfHasInternetConnection(_queryText.value)
        }
    }
}
