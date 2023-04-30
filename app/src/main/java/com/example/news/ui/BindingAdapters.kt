package com.example.news.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.domain.News
import com.example.news.viewmodels.NewsListScreenStatus

@BindingAdapter("image")
fun ImageView.setImage(news: News?) {
    news?.let {
        Glide.with(context)
            .load((news.urlToImage))
            .into(this)
    }
}

@BindingAdapter("content")
fun TextView.setContent(news: News?) {
    news?.let {
        text = news.content
    }
}



@BindingAdapter("statusInformationMessage")
fun TextView.setStatusInformationMessage(newsListScreenStatus: NewsListScreenStatus) {
    if (newsListScreenStatus == NewsListScreenStatus.CONNECTION_PROBLEM) {
        text = resources.getText(R.string.internet_connection_not_available)
    } else if (newsListScreenStatus == NewsListScreenStatus.EMPTY_LIST) {
        text = resources.getText(R.string.empty_news_list_text)
    } else  {
        text = resources.getText(R.string.an_error_occurred_when_trying_to_get_data)
    }
}

