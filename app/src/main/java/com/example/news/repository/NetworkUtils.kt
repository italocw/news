package com.example.news.network

import com.example.news.domain.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.InetAddress

fun parseNewsJsonResult(jsonResult: JSONObject): List<News> {
    val newsArticlesJSONArray = jsonResult.getJSONArray("articles")

    val networkNews = ArrayList<NetworkNews>()
    var newsJSONObject: JSONObject


    for (index in 0 until newsArticlesJSONArray.length()) {
        newsJSONObject = newsArticlesJSONArray.getJSONObject(index)


        newsJSONObject.run {
           /* val title = getString("title")
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
*/


        }

     val    currentNetworkNews =   NewsNetwork.gson.fromJson<NetworkNews>(newsJSONObject.toString(),NetworkNews::class.java)
       // NetworkNewsContainer(networkNews)


        networkNews.add(currentNetworkNews)

    }


    return NetworkNewsContainer(networkNews).asDomainModel()
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
