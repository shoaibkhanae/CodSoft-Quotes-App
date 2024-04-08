package com.example.quotesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.quotesapp.di.QuoteApplication
import com.example.quotesapp.ui.adapter.IdeasAdapter
import com.example.quotesapp.databinding.FragmentFavouriteBinding
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import com.example.quotesapp.ui.viewmodels.QuoteViewModelFactory


class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding
        get() = _binding!!

    private val shareViewModel: QuoteViewModel by activityViewModels {
        QuoteViewModelFactory((requireActivity().application as QuoteApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = IdeasAdapter(shareViewModel)
        binding.favouriteRecyclerview.adapter = adapter

        shareViewModel.savedQuotes.observe(viewLifecycleOwner) { savedQuotes ->
            adapter.submitList(savedQuotes)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}