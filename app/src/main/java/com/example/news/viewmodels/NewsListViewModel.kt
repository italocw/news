package com.example.news.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.Result
import com.example.news.domain.News
import com.example.news.repository.NewsRepository
import com.example.news.ui.news.NewsListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class NewsListScreenStatus {
    LOADING, SUCCESS, CONNECTION_PROBLEM, EMPTY_LIST, ERROR
}


const val DEFAULT_QUERY_TEXT = "Brasil"

class NewsListViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _navigateToNews = MutableLiveData<News?>()
    val navigateToNews
        get() = _navigateToNews


    private val _uiState = MutableStateFlow(NewsListUiState(Result.Loading))
    val uiState: StateFlow<NewsListUiState> = _uiState


    private val _queryText = MutableLiveData<String>()
    val queryText
        get() = _queryText

    init {
        updateScreen()
    }

    private fun updateScreen() {
        viewModelScope.launch {
            _uiState.value =NewsListUiState(Result.Loading)
            newsRepository.updateNewsListFromWeb(getTreatedTextQuery())
            _uiState.value = NewsListUiState(newsRepository.getLastResult())
        }
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
        updateScreen()
    }

    fun onNewsNavigated() {
        _navigateToNews.value = null
    }

    fun refreshNews() {
        if (uiState.value.state != NewsListScreenStatus.LOADING) {
            updateScreen()
        }
    }
}
