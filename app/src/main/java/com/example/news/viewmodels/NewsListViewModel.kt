package com.example.news.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.Result
import com.example.news.domain.News
import com.example.news.repository.NewsRepository
import com.example.news.ui.news.NewsListDataState
import com.example.news.ui.news.NewsListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


const val DEFAULT_QUERY_TEXT = "Brasil"

class NewsListViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _navigateToNews = MutableLiveData<News?>()
    val navigateToNews
        get() = _navigateToNews


    private val _uiState = MutableStateFlow(NewsListUiState(Result.Searching))
    val uiState: StateFlow<NewsListUiState> = _uiState


    private val _queryText = MutableLiveData<String>()
    val queryText
        get() = _queryText

    init {
        _uiState.value.dataState = NewsListDataState.SEARCHING
        updateScreen()
    }

    private fun updateScreen() {
        viewModelScope.launch {
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
        _uiState.value.dataState = NewsListDataState.SEARCHING
        updateScreen()
    }

    fun onNewsNavigated() {
        _navigateToNews.value = null
    }

    fun refreshNews() {
        if (!_uiState.value.isLoading()) {
            _uiState.value.dataState = NewsListDataState.UPDATING
            updateScreen()
        }
    }
}
