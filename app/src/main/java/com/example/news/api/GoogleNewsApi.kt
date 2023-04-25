package com.example.news.api


import com.example.news.BuildConfig.API_KEY
import com.example.news.Constants
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val TOP_HEADLINES_ENDPOINT = "top-headlines?sources=google-news-br&apiKey=".plus(
    API_KEY
)

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(Constants.BASE_API_URL).build()



interface GoogleNewsApiService {
    @GET(TOP_HEADLINES_ENDPOINT)
    suspend fun getTopHeadlines(): String
}

object GoogleNewsApi {
    val retrofitService: GoogleNewsApiService by lazy { retrofit.create(GoogleNewsApiService::class.java) }
}