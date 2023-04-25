package com.example.news.layout.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.api.isInternetAvailable
import com.example.news.models.News
import com.example.news.repository.FetchingState
import com.example.news.repository.Repository
import kotlinx.coroutines.launch

enum class NewsListScreenStatus {
    LOADING, SUCCESS, CONNECTION_PROBLEM, EMPTY_LIST, ERROR
}

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository = Repository()

    private val _navigateToNews = MutableLiveData<News?>()
    val navigateToNews
        get() = _navigateToNews

    private val _news: LiveData<List<News>> =
        repository.news
    val news
        get() = _news

    private val _dataFetchingState: LiveData<FetchingState> = repository.dataFetchingState
    val dataFetchingState
        get() = _dataFetchingState


    private val _screenStatus = MutableLiveData<NewsListScreenStatus>()
    val screenStatus
        get() = _screenStatus

    init {
        fetchOnlineDataIfHasInternetConnection()
    }

    private fun fetchOnlineDataIfHasInternetConnection() {
        viewModelScope.launch {
            _screenStatus.value = NewsListScreenStatus.LOADING

            try {
                if (isInternetAvailable()) {

                    repository.fetchNews()

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

    fun onNewsNavigated() {
        _navigateToNews.value = null
    }

    fun refreshNews() {
        if (screenStatus.value != NewsListScreenStatus.LOADING) {
            fetchOnlineDataIfHasInternetConnection()
        }
    }
}
