package com.example.news.api

import com.example.news.models.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseNewsJsonResult(jsonResult: JSONObject): List<News> {
    val newsArticlesJSONArray = jsonResult.getJSONArray("articles")

    val parsedNews= ArrayList<News>()
    var newsJSONObject: JSONObject


    for (index in 0 until newsArticlesJSONArray.length()) {
        newsJSONObject = newsArticlesJSONArray.getJSONObject(index)


        val title = newsJSONObject.getString("title")
        val url = newsJSONObject.getString("url")
        val urlToImage = newsJSONObject.getString("urlToImage")
        val description = newsJSONObject.getString("description")
        val sourceName = newsJSONObject.getJSONObject("source").getString("name")
        val author = newsJSONObject.getString("author")

        val publishedAt = LocalDateTime.parse(newsJSONObject.getString("publishedAt"), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val content = newsJSONObject.getString("content")


        parsedNews.add(
            News(title, url, urlToImage, description, sourceName, author, publishedAt, content)
        )
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
