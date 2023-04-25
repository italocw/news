package com.example.news.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime


@Parcelize
data class News(
    val title: String,
    val url: String,
    val urlToImage: String,
    val description: String,
    val sourceName:String,
    val author: String,
    val publishedAt: LocalDateTime,
    val content: String) : Parcelable