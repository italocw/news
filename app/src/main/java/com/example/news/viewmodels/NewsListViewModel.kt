package com.example.news.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.news.Result
import com.example.news.domain.News
import com.example.news.network.isInternetAvailable
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.launch

enum class NewsListScreenStatus {
    LOADING, SUCCESS, CONNECTION_PROBLEM, EMPTY_LIST, ERROR
}

const val DEFAULT_QUERY_TEXT = "Brasil"

class NewsListViewModel( private val newsRepository: NewsRepository ) : ViewModel() {

    private val _navigateToNews = MutableLiveData<News?>()
    val navigateToNews
        get() = _navigateToNews

    private val _forceUpdate = MutableLiveData(false)

    private val _news: LiveData<List<News>> =
        _forceUpdate.switchMap { forceUpdate ->
            if (forceUpdate) {
                viewModelScope.launch {
                    tryToUpdateNewsFromWeb()
                }
            }
            newsRepository.observeNewsListFromWeb().switchMap {
                getNewsWithCompleteInformation(it)
            }
        }

    private suspend fun tryToUpdateNewsFromWeb() {
        _screenStatus.value = NewsListScreenStatus.LOADING

        if (isInternetAvailable()) newsRepository.updateNewsListFromWeb(
            getTreatedTextQuery()
        )
        else _screenStatus.value = NewsListScreenStatus.CONNECTION_PROBLEM
    }

    val news
        get() = _news


    private val _screenStatus = MutableLiveData(NewsListScreenStatus.LOADING)
    val screenStatus
        get() = _screenStatus


    private val _queryText = MutableLiveData<String>()
    val queryText
        get() = _queryText


    init {
        _forceUpdate.value = true
    }

    private fun getTreatedTextQuery(): String {
        _queryText.value.let {
            return if (it.isNullOrBlank())
                DEFAULT_QUERY_TEXT
            else it
        }
    }

    fun onNewsClicked(news: News) {
        _navigateToNews.value = news
    }

    fun onNewsTextQuerySubmit() {
        _forceUpdate.value = true
    }

    fun onNewsNavigated() {
        _navigateToNews.value = null
    }

    fun refreshNews() {
        if (screenStatus.value != NewsListScreenStatus.LOADING) {
            _forceUpdate.value = true
        }
    }

    private fun getNewsWithCompleteInformation(newsResult: Result<List<News>>): LiveData<List<News>> {
        val result = MutableLiveData<List<News>>()

        if (newsResult is Result.Success) {
            viewModelScope.launch {
                val newsToDisplay = newsResult.data.filter { it.hasCompleteInformation() }

                _screenStatus.value =
                    if (newsToDisplay.isEmpty())
                        NewsListScreenStatus.EMPTY_LIST
                    else
                        NewsListScreenStatus.SUCCESS

                result.value = newsToDisplay
            }
        } else {
            _screenStatus.value = NewsListScreenStatus.ERROR
        }

        return result
    }
}
