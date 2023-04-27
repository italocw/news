package com.example.news.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.databinding.FragmentNewsDetailBinding


class NewsDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewsDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val news = NewsDetailFragmentArgs.fromBundle(requireArguments()).selectedNews
        binding.news = news

        return binding.root
    }

}

