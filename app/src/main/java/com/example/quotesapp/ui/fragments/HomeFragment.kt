package com.example.quotesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
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
        handleActionBar()
        setupUI()
        binding?.apply {
            homeFragment = this@HomeFragment
        }
    }

    private fun setupUI() {
        val adapter = QuotePagingAdapter()

        /**
         * Handling header and footer loading
         * with retry button
         */
        binding?.recyclerview?.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter{ adapter.retry() },
            footer = LoaderAdapter{ adapter.retry() }
        )

        binding?.btnRetry?.setOnClickListener { adapter.retry() }

        adapter.addLoadStateListener { loadState ->
            binding?.apply {
                progressCircularBar.isVisible = loadState.source.refresh is LoadState.Loading
                btnRetry.isVisible = loadState.mediator?.refresh is LoadState.Error
            }


            val errorState = loadState.mediator?.refresh as? LoadState.Error
            errorState?.let { Toast.makeText(requireContext(),"${it.error}", Toast.LENGTH_LONG).show() }
        }

        shareViewModel.quotes.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle,it)
        }
    }

    private fun handleActionBar() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.layout_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.ideas_button -> {
                        goToIdeasScreen()
                        return true
                    }
                    R.id.search_button -> {
                        goToSearchScreen()
                        return true
                    }
                    R.id.favorite_button -> {
                        goToFavoriteScreen()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun goToCreateScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_createFragment)
    }

    private fun goToIdeasScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_ideasFragment)
    }

    private fun goToSearchScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
    }

    private fun goToFavoriteScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}