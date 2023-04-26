package com.example.news.api

import com.example.news.models.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseNewsJsonResult(jsonResult: JSONObject): List<News> {
    val newsArticlesJSONArray = jsonResult.getJSONArray("articles")

    val parsedNews = ArrayList<News>()
    var newsJSONObject: JSONObject

    var news: News
    for (index in 0 until newsArticlesJSONArray.length()) {
        newsJSONObject = newsArticlesJSONArray.getJSONObject(index)

        newsJSONObject.run {
            val title = getString("title")
            val url = getString("url")
            val urlToImage = getString("urlToImage")
            val description = getString("description")
            val sourceName = getJSONObject("source").getString("name")
            val author = getString("author")

            val publishedAt = LocalDateTime.parse(
                getString("publishedAt"),
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
            val content = getString("content")

            news =    News(title, url, urlToImage, description, sourceName, author, publishedAt, content)

        }

        if (news.hasCompleteInformation()) {
            parsedNews.add(news)
        }

    }

    return parsedNews
}


suspend fun isInternetAvailable(): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }
}
