package com.example.news.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.repository.NewsRepository
import com.example.news.viewmodels.NewsListViewModel

@Suppress("UNCHECKED_CAST")
class NewsListViewModelFactory (
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (NewsListViewModel(newsRepository) as T)
}