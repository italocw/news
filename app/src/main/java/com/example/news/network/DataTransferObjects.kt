package com.example.news.network

import com.example.news.database.DatabaseNews
import com.example.news.domain.News
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class NetworkNewsContainer(
    @SerializedName("articles")
    val networkNewsList: List<NetworkNews>
)

data class NetworkNews(
    val title: String,
    val url: String,
    val urlToImage: String,
    val description: String,
    val source: Source,
    val author: String,
    val publishedAt: String,
    val content: String

)

data class Source(
    val id: String,
    val name: String
)


fun NetworkNewsContainer.asDomainModel(): List<News> {

    return networkNewsList!!.map {
        News(
            it.title,
            it.url,
            it.urlToImage,
            it.description,
            it.source.name,
            it.author,
            LocalDateTime.parse(
                it.publishedAt,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            ),
            it.content
        )
    }
}

fun NetworkNewsContainer.asDatabaseModel(): List<DatabaseNews> {
    return networkNewsList.map {
        DatabaseNews(
            it.title,
            it.url,
            it.urlToImage,
            it.description,
            it.source.name,
            it.author,
            it.publishedAt,
            it.content
        )
    }
}