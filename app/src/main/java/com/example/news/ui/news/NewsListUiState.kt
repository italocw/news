package com.example.news.ui.news

import com.example.news.R
import com.example.news.Result
import com.example.news.domain.News
import com.example.news.network.InternetMissingException
import com.example.news.succeeded
import com.example.news.viewmodels.NewsListResultState


class NewsListUiState(private val newsList: Result<List<News>>) {
    var dataState: NewsListResultState
    var newsWithCompleteInformationList: List<News>

    init {
        newsWithCompleteInformationList = getNewsWithCompleteInformation()
        dataState = getStatus()
    }

    fun isLoading() =
        dataState == NewsListResultState.UPDATING || dataState == NewsListResultState.SEARCHING


   private fun getNewsWithCompleteInformation(): List<News> {
        return if (newsList.succeeded) {
            (newsList as Result.Success).data.filter {
                it.hasCompleteInformation()
            }
        } else {
            listOf()
        }
    }

    fun getUserInformationMessage(): Int? {
        return when (dataState) {
            NewsListResultState.SEARCHING -> R.string.getting_news
            NewsListResultState.CONNECTION_PROBLEM -> R.string.internet_connection_not_available
            NewsListResultState.EMPTY_LIST -> R.string.empty_news_list_text
            NewsListResultState.ERROR -> R.string.an_error_occurred_when_trying_to_get_data
            NewsListResultState.SUCCESS, NewsListResultState.UPDATING -> null
        }
    }


    private fun getStatus(): NewsListResultState {
        return when (newsList) {
            is Result.Searching -> NewsListResultState.SEARCHING
            is Result.Error -> getErrorUserStatus()
            is Result.Success -> return if (newsWithCompleteInformationList.isNullOrEmpty()) {
                NewsListResultState.EMPTY_LIST
            } else {
                NewsListResultState.SUCCESS
            }

        }
    }


    private fun getErrorUserStatus(): NewsListResultState {
        val exception = (newsList as Result.Error).exception

        return when (exception) {
            is InternetMissingException -> NewsListResultState.CONNECTION_PROBLEM
            else -> NewsListResultState.ERROR
        }

    }

    fun listWithNewsIsShown(): Boolean = !newsWithCompleteInformationList.isNullOrEmpty()

}