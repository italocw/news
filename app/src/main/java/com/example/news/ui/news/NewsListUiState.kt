package com.example.news.ui.news

import com.example.news.R
import com.example.news.Result
import com.example.news.domain.News
import com.example.news.network.InternetMissingException
import com.example.news.succeeded

enum class NewsListDataState {
    REFRESHING, POPULATED, CONNECTION_PROBLEM, EMPTY_LIST, ERROR, SEARCHING
}

class NewsListUiState(private val newsList: Result<List<News>>, ) {

    var dataState: NewsListDataState
    var newsWithCompleteInformationList: List<News>

    init {
        newsWithCompleteInformationList = getNewsWithCompleteInformation()
        dataState = getStatus()
    }

    fun isLoading() =
        dataState == NewsListDataState.REFRESHING || dataState == NewsListDataState.SEARCHING


   private fun getNewsWithCompleteInformation(): List<News> {
        return if (newsList.succeeded) {
            (newsList as Result.Success).data.filter {
                it.hasCompleteInformation()
            }
        } else {
            listOf()
        }
    }

    fun getInformationMessage(): Int? {
        return when (dataState) {
            NewsListDataState.SEARCHING -> R.string.getting_news
            NewsListDataState.CONNECTION_PROBLEM -> R.string.internet_connection_not_available
            NewsListDataState.EMPTY_LIST -> R.string.empty_news_list_text
            NewsListDataState.ERROR -> R.string.an_error_occurred_when_trying_to_get_data
            NewsListDataState.POPULATED, NewsListDataState.REFRESHING -> null
        }
    }


    private fun getStatus(): NewsListDataState {
        return when (newsList) {
            is Result.Searching -> NewsListDataState.SEARCHING
            is Result.Error -> getErrorUserStatus()
            is Result.Success -> return if (newsWithCompleteInformationList.isNullOrEmpty()) {
                NewsListDataState.EMPTY_LIST
            } else {
                NewsListDataState.POPULATED
            }

        }
    }


    private fun getErrorUserStatus(): NewsListDataState {
        val exception = (newsList as Result.Error).exception

        return when (exception) {
            is InternetMissingException -> NewsListDataState.CONNECTION_PROBLEM
            else -> NewsListDataState.ERROR
        }

    }

    fun listWithNewsIsShown(): Boolean = !newsWithCompleteInformationList.isNullOrEmpty()

}