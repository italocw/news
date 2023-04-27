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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.news.R
import com.example.news.databinding.FragmentNewsListBinding
import com.example.news.viewmodels.MainViewModel
import com.example.news.viewmodels.NewsListScreenStatus

class NewsListFragment : Fragment() {
    private lateinit var binding: FragmentNewsListBinding
    private lateinit var newsListAdapter: NewsAdapter
    private lateinit var viewModel: MainViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var loadingDataLayout: View
    private lateinit var informationMessageTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsListBinding.inflate(inflater)
        binding.lifecycleOwner = this

        NewsAdapter(NewsAdapter.NewsListener { news ->
            viewModel.onNewsClicked(news)
        }).apply { newsListAdapter = this }

        binding.newsRecycler.adapter = newsListAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.viewModel = viewModel

        setViews(binding)
        setObservers()

        swipeRefreshLayout.setOnRefreshListener { refresh() }

        return binding.root
    }


    private fun setViews(binding: FragmentNewsListBinding) {
        binding.let {
            swipeRefreshLayout = it.swipeRefresh
            newsRecyclerView = it.newsRecycler
            loadingDataLayout = it.statusLoadingWheel
            informationMessageTextView = it.informationMessage
        }
    }

    private fun setObservers() {
        setNewsClickObserver()
        setScreenStatusObserver()
        setNewsListObserver()
    }

    private fun setScreenStatusObserver() {
        viewModel.screenStatus.observe(viewLifecycleOwner) { screenStatus ->
            setScreenByDisplayingStatus(screenStatus)
        }
    }

    private fun setScreenByDisplayingStatus(screenStatus: NewsListScreenStatus) {

        when (screenStatus) {
            NewsListScreenStatus.SUCCESS -> setScreenAsUpdatedListWithNews()

            NewsListScreenStatus.CONNECTION_PROBLEM, NewsListScreenStatus.EMPTY_LIST, NewsListScreenStatus.ERROR ->
                setScreenAsNoNewsAreBeingShown()

            NewsListScreenStatus.LOADING -> setScreenAsLoadingData()
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

    private fun listWithNewsIsShown() = !viewModel.news.value.isNullOrEmpty()

    private fun setScreenAsUpdatedListWithNews() {
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = true
        newsRecyclerView.visibility = VISIBLE
        loadingDataLayout.visibility = GONE

        informationMessageTextView.visibility = GONE
    }

    private fun setScreenAsNoNewsAreBeingShown() {
        loadingDataLayout.visibility = GONE
        swipeRefreshLayout.isRefreshing = false
        newsRecyclerView.visibility = GONE
        informationMessageTextView.visibility = VISIBLE
        swipeRefreshLayout.isEnabled = true
    }

    private fun setNewsClickObserver() {
        viewModel.navigateToNews.observe(viewLifecycleOwner) { news ->

            news?.let {
                this.findNavController()
                    .navigate(NewsListFragmentDirections.actionShowDetail(news, news.sourceName))
                viewModel.onNewsNavigated()
            }
        }
    }

    private fun setNewsListObserver() {
        viewModel.news.observe(viewLifecycleOwner) { newsList ->
            newsListAdapter.submitList(newsList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                    val searchView = menuItem.actionView as SearchView

                    searchView.isIconified = false
                    searchView.queryHint = viewModel.queryText.value ?: getString(R.string.search)
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setSearchViewListeners(searchView: SearchView) {
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.queryText.value = newText
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.onNewsTextQuerySubmit()
                    return true
                }
            }

        searchView.setOnQueryTextListener(queryTextListener)
    }

    fun refresh() {
        this.viewModel.refreshNews()
    }
}
