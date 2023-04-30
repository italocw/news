package com.example.news.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentNewsListBinding
import com.example.news.repository.NewsRepository
import com.example.news.viewmodels.NewsListScreenStatus
import com.example.news.viewmodels.NewsListViewModel

class NewsListFragment : Fragment() {
    private val newsListViewModel by viewModels<NewsListViewModel>() {
        NewsListViewModelFactory(NewsRepository.getRepository(requireActivity().application))
    }

    private lateinit var viewBinding: FragmentNewsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNewsListBinding.inflate(inflater, container, false).apply {
            viewModel = newsListViewModel
        }

        return viewBinding.root
    }


    private fun setViews() {
        setNewsList()
        setMenu()

        viewBinding.swipeRefresh.setOnRefreshListener { refresh() }
    }

    private fun setObservers() {
        setNewsClickObserver()
        setScreenStatusObserver()
        setNewsListObserver()
    }

    private fun setScreenStatusObserver() {
        newsListViewModel.screenStatus.observe(viewLifecycleOwner) { screenStatus ->
            setScreenByDisplayingStatus(screenStatus)
        }
    }

    private fun setScreenByDisplayingStatus(screenStatus: NewsListScreenStatus) {
        when (screenStatus) {
            NewsListScreenStatus.SUCCESS -> setScreenAsUpdatedWithNews()
            NewsListScreenStatus.LOADING -> setScreenAsLoadingData()
            else -> setScreenAsLoadedWithoutNews()
        }
    }


    private fun setScreenAsLoadingData() {
        viewBinding.apply {
            informationMessage.visibility = GONE

            if (listWithNewsIsShown()) {
                swipeRefresh.isRefreshing = true

            } else {
                swipeRefresh.isEnabled = false
                loadingDataLayout.visibility = VISIBLE
            }
        }
    }

    private fun listWithNewsIsShown() = !newsListViewModel.news.value.isNullOrEmpty()

    private fun setScreenAsUpdatedWithNews() {
        viewBinding.apply {
            swipeRefresh.isRefreshing = false
            swipeRefresh.isEnabled = true
            newsRecycler.visibility = VISIBLE
            loadingDataLayout.visibility = GONE
            informationMessage.visibility = GONE
        }
    }

    private fun setScreenAsLoadedWithoutNews() {
        viewBinding.apply {
            loadingDataLayout.visibility = GONE
            swipeRefresh.isRefreshing = false
            newsRecycler.visibility = GONE
            informationMessage.visibility = VISIBLE
            swipeRefresh.isEnabled = true
        }
    }

    private fun setNewsClickObserver() {
        newsListViewModel.navigateToNews.observe(viewLifecycleOwner) { news ->

            news?.let {
                this.findNavController()
                    .navigate(NewsListFragmentDirections.actionShowDetail(news, news.sourceName))
                newsListViewModel.onNewsNavigated()
            }
        }
    }

    private fun setNewsListObserver() {
        newsListViewModel.news.observe(viewLifecycleOwner) { newsList ->
            (viewBinding.newsRecycler.adapter as NewsAdapter).submitList(newsList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        setViews()
        setObservers()
    }

    private fun setMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_news_list_menu, menu)
                val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
                setSearchViewListeners(searchView)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_refresh) {
                    refresh()
                } else {
                    (menuItem.actionView as SearchView).apply {
                        isIconified = false
                        queryHint = getString(R.string.search)
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setNewsList() {
        NewsAdapter(NewsAdapter.NewsListener { news ->
            newsListViewModel.onNewsClicked(news)
        }).apply { viewBinding.newsRecycler.adapter = this }
    }

    private fun setSearchViewListeners(searchView: SearchView) {
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    newsListViewModel.queryText.value = newText
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    newsListViewModel.onNewsTextQuerySubmit()
                    return true
                }
            }

        searchView.setOnQueryTextListener(queryTextListener)
    }

    fun refresh() {
        this.newsListViewModel.refreshNews()
    }
}
