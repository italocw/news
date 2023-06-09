package com.example.news.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.news.domain.News
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "News")
data class DatabaseNews constructor(
    @PrimaryKey
    val url: String,
    val title: String,
    val urlToImage: String,
    val description: String,
    val sourceName: String,
    val author: String,
    val publishedAt: Long,
    val content: String
) {
    fun asDomainModel() = News(
        url,
        title,
        urlToImage,
        description,
        sourceName,
        author,
        Instant.ofEpochMilli(publishedAt),
        content
    )
}

fun List<DatabaseNews>.asDomainModel(): List<News> {
    return map {
        News(
            it.url,
            it.title,
            it.urlToImage,
            it.description,
            it.sourceName,
            it.author,
            Instant.ofEpochMilli(it.publishedAt),
            it.content
        )
    }
}
