package com.example.news.layout.main

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.example.news.databinding.FragmentMainBinding
import com.example.news.R
import com.example.news.NewsAdapter

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var ticketListAdapter: NewsAdapter
    private lateinit var viewModel: MainViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var ticketRecyclerView: RecyclerView
    private lateinit var loadingDataLayout: View
    private lateinit var informationMessageTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        NewsAdapter(NewsAdapter.NewsListener { ticket ->
            viewModel.onNewsClicked(ticket)
        }).apply { ticketListAdapter = this }

        binding.ticketRecycler.adapter = ticketListAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.viewModel = viewModel

        setViews(binding)
        setObservers()

        swipeRefreshLayout.setOnRefreshListener { refresh() }

        return binding.root
    }

    private fun setViews(binding: FragmentMainBinding) {
        swipeRefreshLayout = binding.swipeRefresh
        ticketRecyclerView = binding.ticketRecycler
        loadingDataLayout = binding.statusLoadingWheel
        informationMessageTextView = binding.informationMessage
    }

    private fun setObservers() {
        setTicketClickObserver()
        setScreenStatusObserver()
        setTicketListObserver()
    }

    private fun setScreenStatusObserver() {
        viewModel.screenStatus.observe(viewLifecycleOwner, Observer { screenStatus ->
            setScreenByDisplayingStatus(screenStatus)
        })
    }

    private fun setScreenByDisplayingStatus(screenStatus: NewsListScreenStatus) {
        when (screenStatus) {
            NewsListScreenStatus.SUCCESS -> {
                setScreenAsUpdatedListWithTickets()
            }
            NewsListScreenStatus.CONNECTION_PROBLEM, NewsListScreenStatus.EMPTY_LIST, NewsListScreenStatus.ERROR -> {
                setScreenAsNoTicketsAreBeingShown()
            }
            NewsListScreenStatus.LOADING -> setScreenAsLoadingData()
        }
    }

    private fun setScreenAsLoadingData() {
        informationMessageTextView.visibility = GONE

        if (listWithTicketsIsShown()) {
            swipeRefreshLayout.isRefreshing = true

        } else {
            swipeRefreshLayout.isEnabled = false
            loadingDataLayout.visibility = VISIBLE
        }
    }

    private fun listWithTicketsIsShown() =
        !viewModel.news.value.isNullOrEmpty()

    private fun setScreenAsUpdatedListWithTickets() {
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = true
        ticketRecyclerView.visibility = VISIBLE
        loadingDataLayout.visibility = GONE

        informationMessageTextView.visibility = GONE
    }

    private fun setScreenAsNoTicketsAreBeingShown() {
        loadingDataLayout.visibility = GONE
        swipeRefreshLayout.isRefreshing = false
        ticketRecyclerView.visibility = GONE
        informationMessageTextView.visibility = VISIBLE
        swipeRefreshLayout.isEnabled = true
    }

    private fun setTicketClickObserver() {
        viewModel.navigateToNews.observe(viewLifecycleOwner, Observer { news ->

            news?.let {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionShowDetail(news, news.url))
                viewModel.onNewsNavigated()
            }
        })
    }

    private fun setTicketListObserver() {
        viewModel.news.observe(viewLifecycleOwner) { newsList ->
            ticketListAdapter.submitList(newsList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_news_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                refresh()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun refresh() {
        this.viewModel.refreshNews()
    }
}
