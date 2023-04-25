package com.example.news

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.news.layout.main.NewsListScreenStatus
import com.example.news.models.News
import com.example.news.repository.FetchingState


@BindingAdapter("image")
fun ImageView.setImage(news: News?) {
    news?.let {
        Glide.with(context)
            .load((news.urlToImage))
            .into(this)
    }
}


@BindingAdapter("dataFetchingStatus")
fun TextView.setFetchingState(dataFetchingStatus: FetchingState?) {
    if (dataFetchingStatus == FetchingState.DOWNLOADING_NEWS) {
        text = resources.getText(R.string.getting_news)
    }
}

@BindingAdapter("statusInformationMessage")
fun TextView.setStatusInformationMessage(newsListScreenStatus: NewsListScreenStatus) {

    if (newsListScreenStatus == NewsListScreenStatus.CONNECTION_PROBLEM) {
        text = resources.getText(R.string.internet_connection_not_available)
    } else if (newsListScreenStatus == NewsListScreenStatus.EMPTY_LIST) {
        text = resources.getText(R.string.empty_news_list_text)
    } else if (newsListScreenStatus == NewsListScreenStatus.ERROR) {
        text = resources.getText(R.string.an_error_occurred_when_trying_to_get_data)
    }
}

