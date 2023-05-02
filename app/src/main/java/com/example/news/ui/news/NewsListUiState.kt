package com.example.news.ui.news

import com.example.news.R
import com.example.news.Result
import com.example.news.domain.News
import com.example.news.network.InternetMissingException
import com.example.news.succeeded
import com.example.news.viewmodels.NewsListScreenStatus


data class NewsListUiState(private val newsList: Result<List<News>>) {
    val state: NewsListScreenStatus
    val informationMessage: Int?
    val newsWithCompleteInformationList: List<News>

    init {
        newsWithCompleteInformationList = getNewsWithCompleteInformation()
        state = getStatus()
        informationMessage = getUserInformationMessage()
    }

    fun getNewsWithCompleteInformation(): List<News> {
        return if (newsList.succeeded) {
            (newsList as Result.Success).data.filter {
                it.hasCompleteInformation()
            }
        } else {
            listOf()
        }
    }

    private fun getUserInformationMessage(): Int? {
        return when (state) {
            NewsListScreenStatus.LOADING -> R.string.getting_news
            NewsListScreenStatus.CONNECTION_PROBLEM -> R.string.internet_connection_not_available
            NewsListScreenStatus.EMPTY_LIST -> R.string.empty_news_list_text
            NewsListScreenStatus.ERROR -> R.string.an_error_occurred_when_trying_to_get_data
            NewsListScreenStatus.SUCCESS -> null

        }
    }


    private fun getStatus(): NewsListScreenStatus {
        return when (newsList) {
            is Result.Loading -> NewsListScreenStatus.LOADING
            is Result.Error -> getErrorUserStatus()
            is Result.Success -> return if (newsWithCompleteInformationList.isNullOrEmpty()) {
                NewsListScreenStatus.EMPTY_LIST
            } else {
                NewsListScreenStatus.SUCCESS
            }
        }
    }


    private fun getErrorUserStatus(): NewsListScreenStatus {
        val exception = (newsList as Result.Error).exception

        return when (exception) {
            is InternetMissingException -> NewsListScreenStatus.CONNECTION_PROBLEM
            else -> NewsListScreenStatus.ERROR
        }

    }

}