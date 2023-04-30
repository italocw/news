package com.example.news

import android.app.Application
import com.example.news.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@NewsApplication)
            modules(appModule)
        }
    }
}
