package com.example.news.ui.news

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.databinding.FragmentNewsListBinding
import com.example.news.viewmodels.NewsListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class NewsListFragment : Fragment() {
    private val newsListViewModel by activityViewModel<NewsListViewModel>()
    private lateinit var viewBinding: FragmentNewsListBinding
    private lateinit var screenState: NewsListUiState
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNewsListBinding.inflate(inflater, container, false).apply {
            viewModel = newsListViewModel
        }

        setScreenState()
        return viewBinding.root
    }


    private fun setViews() {
        setNewsList()
        setMenu()

        viewBinding.seeAllNewsButton.setOnClickListener { newsListViewModel.updateWithDefaultSearch() }
        viewBinding.swipeRefresh.setOnRefreshListener { refresh() }
    }

    private fun setObservers() {
        setNewsClickObserver()
    }

    private fun setScreenState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsListViewModel.uiState.collectLatest {
                    screenState = it
                    setScreenContentByState()
                }
            }
        }
    }

    private fun setScreenContentByState() {
        if (screenState.dataState == NewsListDataState.POPULATED) {
            setScreenAsUpdatedWithNews()
        } else if (screenState.isLoading()) {
            setScreenAsLoadingData()
        } else setScreenAsLoadedWithoutNews()
    }

    private fun setScreenAsLoadingData() {
        viewBinding.apply {
            seeAllNewsButton.visibility = GONE
            if (screenState.listWithNewsIsShown()) {
                swipeRefresh.isRefreshing = true
             
            } else {
                swipeRefresh.isEnabled = false
                progressBar.visibility = VISIBLE
            }
        }
    }

    private fun setScreenAsUpdatedWithNews() {
        viewBinding.apply {
            (newsRecycler.adapter as NewsAdapter).submitList(screenState.newsWithCompleteInformationList)

            letSwipeRefreshAvailable()
            newsRecycler.visibility = VISIBLE
            progressBar.visibility = GONE
            statusText.visibility = GONE
            seeAllNewsButton.visibility = GONE

        }
    }

    private fun setScreenAsLoadedWithoutNews() {
        viewBinding.apply {

            progressBar.visibility = GONE
            newsRecycler.visibility = GONE
            statusText.visibility = VISIBLE
            seeAllNewsButton.visibility = VISIBLE
        }
    }

    private fun FragmentNewsListBinding.letSwipeRefreshAvailable() {
        swipeRefresh.isRefreshing = false
        swipeRefresh.isEnabled = true
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
                    hideKeyboard()

                    return true
                }

                private fun hideKeyboard() {
                    val inputMethodManager =
                        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
                }
            }

        searchView.setOnQueryTextListener(queryTextListener)
    }

    fun refresh() {
        this.newsListViewModel.refreshNews()
    }
}
