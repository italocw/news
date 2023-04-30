package com.example.news.network


import com.example.news.BuildConfig.API_KEY
import com.example.news.Constants
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.InetAddress


interface GoogleNewsApiService {
    @GET("everything?language=pt&apiKey=".plus(API_KEY))
    suspend fun getEverythingNews(@Query("q") queryText:String ): Response<NetworkNewsContainer>

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