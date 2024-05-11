package com.example.quotesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentFavouriteBinding
import com.example.quotesapp.di.QuoteApplication
import com.example.quotesapp.ui.adapters.IdeasAdapter
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import com.example.quotesapp.ui.viewmodels.QuoteViewModelFactory
import com.google.android.material.snackbar.Snackbar


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

        val adapter = IdeasAdapter()
        binding.favouriteRecyclerview.adapter = adapter

        adapter.setOnItemClickListener(object : IdeasAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val current = adapter.currentList[position]

                val action = FavouriteFragmentDirections
                    .actionFavouriteFragmentToQuoteFragment(current.content, current.author)
                findNavController().navigate(action)
            }

        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val quote = adapter.currentList[position]
                shareViewModel.delete(quote)

                Snackbar.make(view,"Successfully deleted quote", Snackbar.LENGTH_LONG).apply {
                    setAction("undo"){
                        shareViewModel.insert(quote)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.favouriteRecyclerview)
        }

        shareViewModel.savedQuotes.observe(viewLifecycleOwner) { savedQuotes ->
            adapter.submitList(savedQuotes)
        }

        binding.favouriteAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_favouriteFragment_to_homeFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}