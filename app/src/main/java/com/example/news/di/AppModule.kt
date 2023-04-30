package com.example.news.di

import com.example.news.Constants
import com.example.news.network.GoogleNewsApiService
import com.example.news.network.NewsRemoteDataSource
import com.example.news.repository.NewsRepository
import com.example.news.viewmodels.NewsListViewModel
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    viewModelOf(::NewsListViewModel)

    single {
        NewsRepository.getRepository(androidApplication(), get())
    }

    single { provideRetrofit() }
    single { provideApiService(get()) }
    single { NewsRemoteDataSource(get()) }

}

private fun provideRetrofit(): Retrofit {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val gson = GsonBuilder().create()

    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(Constants.BASE_API_URL)
        .build()
}

private fun provideApiService(retrofit: Retrofit): GoogleNewsApiService =
    retrofit.create(GoogleNewsApiService::class.java)


