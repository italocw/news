package com.example.news.domain

import android.os.Parcelable
import com.example.news.database.DatabaseNews
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class News(
    val title: String?,
    val url: String,
    val urlToImage: String?,
    val description: String?,
    val sourceName: String,
    val author: String?,
    val publishedAt: LocalDateTime,
    val content: String?
) : Parcelable {
    fun hasCompleteInformation() = null !in listOf(
        title,
        description,
        urlToImage,
        sourceName,
        author,
        content)

}