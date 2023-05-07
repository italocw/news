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
        updateScreenWithDefaultSearch()
    }

    private fun updateScreenWithNewSearch() {
        _uiState.value.dataState = NewsListDataState.SEARCHING
        viewModelScope.launch {
            val result = newsRepository.getNewsFromWebByQuery(getTreatedTextQuery())
            _uiState.value = NewsListUiState(result)
        }
    }

    private fun updateScreenWithDefaultSearch() {
        _uiState.value.dataState = NewsListDataState.SEARCHING
        viewModelScope.launch {
            val result = newsRepository.getNewsFromWebByQuery(DEFAULT_QUERY_TEXT)
            _uiState.value = NewsListUiState(result)
        }
    }

    private fun refreshSearchOnScreen() {
        _uiState.value.dataState = NewsListDataState.REFRESHING
        viewModelScope.launch {
            val result = newsRepository.refreshCurrentSearchFromWeb()
            _uiState.value = NewsListUiState(result)
        }
    }

    private fun getTreatedTextQuery(): String {
        return _queryText.value.takeIf {
            !it.isNullOrBlank()
        } ?: DEFAULT_QUERY_TEXT
    }

    fun onNewsClicked(news: News) {
        _navigateToNews.value = news
    }

    fun onNewsTextQuerySubmit() {
        _uiState.value.dataState = NewsListDataState.SEARCHING
        updateScreenWithNewSearch()
    }

    fun onNewsNavigated() {
        _navigateToNews.value = null
    }

    fun refreshNews() {
        if (!_uiState.value.isLoading()) {
            refreshSearchOnScreen()
        }
    }

    fun updateWithDefaultSearch() {

        updateScreenWithDefaultSearch()
    }
}
