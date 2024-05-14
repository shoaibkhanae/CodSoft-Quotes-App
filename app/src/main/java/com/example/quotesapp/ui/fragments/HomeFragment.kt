package com.example.quotesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.quotesapp.R
import com.example.quotesapp.data.paging.adapter.LoaderAdapter
import com.example.quotesapp.data.paging.adapter.QuotePagingAdapter
import com.example.quotesapp.databinding.FragmentHomeBinding
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding

    private val shareViewModel: QuoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QuotePagingAdapter()

        binding?.recyclerview?.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter{ adapter.retry() },
            footer = LoaderAdapter{ adapter.retry() }
        )

        binding?.btnRetry?.setOnClickListener { adapter.retry() }

        // Handling the loading states with paging
        adapter.addLoadStateListener { loadState ->
            binding?.progressCircularBar?.isVisible = loadState.refresh is LoadState.Loading
            binding?.btnRetry?.isVisible = loadState.refresh is LoadState.Error

            val errorState = loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            errorState?.let { Toast.makeText(requireContext(),"${it.error}",Toast.LENGTH_LONG).show() }
        }

        shareViewModel.quotes.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle,it)
        }


        binding?.ideasIcon?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
        }

        binding?.searchIcon?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}