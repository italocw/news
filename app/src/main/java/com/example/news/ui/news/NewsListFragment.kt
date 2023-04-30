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
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.news.R
import com.example.news.databinding.FragmentNewsListBinding
import com.example.news.repository.NewsRepository
import com.example.news.viewmodels.NewsListScreenStatus
import com.example.news.viewmodels.NewsListViewModel

class NewsListFragment : Fragment() {
    private val newsListViewModel by viewModels<NewsListViewModel>() {
        NewsListViewModelFactory(NewsRepository.getRepository(requireActivity().application))
    }

    private lateinit var binding: FragmentNewsListBinding
    private lateinit var newsListAdapter: NewsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var loadingDataLayout: View
    private lateinit var informationMessageTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsListBinding.inflate(inflater, container, false).apply {
            viewModel = newsListViewModel
        }

        return binding.root
    }


    private fun setViews() {
        setNewsList()
        setMenu()

        binding.let {
            swipeRefreshLayout = it.swipeRefresh
            newsRecyclerView = it.newsRecycler
            loadingDataLayout = it.statusLoadingWheel
            informationMessageTextView = it.informationMessage
        }
        swipeRefreshLayout.setOnRefreshListener { refresh() }
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
        informationMessageTextView.visibility = GONE

        if (listWithNewsIsShown()) {
            swipeRefreshLayout.isRefreshing = true

        } else {
            swipeRefreshLayout.isEnabled = false
            loadingDataLayout.visibility = VISIBLE
        }
    }

    private fun listWithNewsIsShown() = !newsListViewModel.news.value.isNullOrEmpty()

    private fun setScreenAsUpdatedWithNews() {
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = true
        newsRecyclerView.visibility = VISIBLE
        loadingDataLayout.visibility = GONE

        informationMessageTextView.visibility = GONE
    }

    private fun setScreenAsLoadedWithoutNews() {
        loadingDataLayout.visibility = GONE
        swipeRefreshLayout.isRefreshing = false
        newsRecyclerView.visibility = GONE
        informationMessageTextView.visibility = VISIBLE
        swipeRefreshLayout.isEnabled = true
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
            newsListAdapter.submitList(newsList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this.viewLifecycleOwner

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
        }).apply { newsListAdapter = this }

        binding.newsRecycler.adapter = newsListAdapter
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
