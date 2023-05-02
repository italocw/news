package com.example.news.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

import com.example.news.R
import com.example.news.domain.News

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

@BindingAdapter("resultInformationMessage")
fun TextView.setResultInformationMessage(informationTextResource: Int?) {
    if (informationTextResource != null) {
            text=  context.getText(informationTextResource)
        }

}

