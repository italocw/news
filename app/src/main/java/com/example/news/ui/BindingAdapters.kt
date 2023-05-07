package com.example.news.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

import com.example.news.R
import com.example.news.domain.News
import com.example.news.ui.news.NewsListDataState

@BindingAdapter("image")
fun ImageView.setImage(news: News?) {
    news?.let {
        val placeholder = R.drawable.news_image_placeholder
        this.load(news.urlToImage) {
            placeholder(placeholder)
            error(placeholder)
        }
    }
}

@BindingAdapter("statusMessage")
fun TextView.setStatusMessage(statusMessage: Int?) {
    if (statusMessage != null) {
        text = context.getText(statusMessage)
    }

}

@BindingAdapter("buttonMessage")
fun TextView.setButtonMessage(screenStatus: NewsListDataState) {
    if (screenStatus == NewsListDataState.CONNECTION_PROBLEM) {
        text = context.getText(R.string.reload)
    } else {
        text = context.getText(R.string.see_all_news)
    }
}

