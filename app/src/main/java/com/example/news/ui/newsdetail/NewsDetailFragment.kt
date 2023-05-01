package com.example.news.ui.newsdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.databinding.FragmentNewsDetailBinding
import com.example.news.ui.newsdetail.NewsDetailFragmentArgs.fromBundle
import com.google.accompanist.themeadapter.material.MdcTheme


class NewsDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewsDetailBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner


            newsDetailComposeView.apply {
                setContent {
                    MdcTheme() {
                        NewsDetailDescription(fromBundle(requireArguments()).selectedNews)
                    }
                }
            }
        }



        return binding.root
    }

}

