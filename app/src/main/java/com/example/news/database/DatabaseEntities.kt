package com.example.news.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.news.domain.News
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */


/**
 * DatabaseVideo represents a video entity in the database.
 */
@Entity
data class DatabaseNews constructor(
    @PrimaryKey
    val url: String,
    val title: String,
    val urlToImage: String,
    val description: String,
    val sourceName: String,
    val author: String,
    val publishedAt: String,
    val content: String
)


/**
 * Map DatabaseVideos to domain entities
 */
fun List<DatabaseNews>.asDomainModel(): List<News> {
    return map {
        News(
            it.url,
            it.title,
            it.urlToImage,
            it.description,
            it.sourceName,
            it.author,
            LocalDateTime.parse(
                it.publishedAt,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            ),
            it.content
        )
    }
}
