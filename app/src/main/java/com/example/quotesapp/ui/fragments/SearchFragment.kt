package com.example.quotesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.databinding.FragmentSearchBinding
import com.example.quotesapp.ui.adapters.SearchAdapter
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import com.example.quotesapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private val shareViewModel: QuoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater,container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchQuotes()
        setupUI()
    }

    private fun searchQuotes() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString()
                shareViewModel.searchQuotes(query)
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
        }
    }

    private fun setupUI() {
        shareViewModel.searched.observe(viewLifecycleOwner) {
            when(it) {
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerview.visibility = View.VISIBLE

                    val adapter = it.data?.results?.let { list -> SearchAdapter(list) }
                    binding.recyclerview.adapter = adapter

                    // for item click
                    adapter?.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val current = it.data.results[position]
                            val action = SearchFragmentDirections
                                .actionSearchFragmentToQuoteFragment(current.content, current.author)
                            findNavController().navigate(action)
                        }
                    })

                }
                is Response.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerview.visibility = View.GONE
                    Toast.makeText(requireContext(),it.error.toString(),Toast.LENGTH_LONG).show()
                }
                is Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}