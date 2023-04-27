package com.example.news.network


import com.example.news.BuildConfig.API_KEY
import com.example.news.Constants
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleNewsApiService {
    @GET("everything?language=pt&apiKey=".plus(API_KEY))
    suspend fun getEverythingNews(@Query("q") queryText:String ): Response<NetworkNewsContainer>

}

object NewsNetwork {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(Constants.BASE_API_URL).build()



    val newsService: GoogleNewsApiService by lazy { retrofit.create(GoogleNewsApiService::class.java) }
}