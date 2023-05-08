package com.example.news.di

import android.content.Context
import androidx.room.Room
import com.example.news.Constants
import com.example.news.NewsApplication
import com.example.news.database.NewsDao
import com.example.news.database.NewsDatabase
import com.example.news.database.NewsLocalDataSource
import com.example.news.domain.News
import com.example.news.network.GoogleNewsApiService
import com.example.news.network.NewsRemoteDataSource
import com.example.news.repository.NewsRepository
import com.example.news.viewmodels.NewsListViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    viewModelOf(::NewsListViewModel)

    single {
        NewsRepository.getRepository(get(), get())
    }

    single { provideRetrofit() }
    single { provideApiService(get()) }
    single { NewsRemoteDataSource(get()) }
    single { NewsLocalDataSource(get(), Dispatchers.IO) }
    single { provideTicketDao( androidContext()) }

}
private fun provideTicketDao(context: Context): NewsDao =
    NewsDatabase.getInstance(context).newsDao


private fun provideRetrofit(): Retrofit {
    val gson = GsonBuilder().create()

    return Retrofit.Builder()

        .addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(Constants.BASE_API_URL)
        .build()
}

private fun provideApiService(retrofit: Retrofit): GoogleNewsApiService =
    retrofit.create(GoogleNewsApiService::class.java)

