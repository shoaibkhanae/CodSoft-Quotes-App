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
import com.example.quotesapp.databinding.FragmentIdeasBinding
import com.example.quotesapp.ui.adapters.AuthorsAdapter
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IdeasFragment : Fragment() {
    private var _binding: FragmentIdeasBinding? = null
    private val binding
        get() = _binding!!

    private val shareViewModel: QuoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdeasBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val adapter = AuthorsAdapter()
        binding.recyclerview.adapter = adapter

        adapter.setOnItemClickListener(object : AuthorsAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val current = adapter.currentList[position]

                val action = IdeasFragmentDirections
                    .actionIdeasFragmentToUpdateFragment(current.id,current.author,current.content)
                findNavController().navigate(action)
            }
        })


        /**
         * For enabling swipe to delete feature in list
         */
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
                val write = adapter.currentList[position]
                shareViewModel.delete(write)

                view?.let {
                    Snackbar.make(it,"Successfully deleted quote", Snackbar.LENGTH_LONG).apply {
                        setAction("undo"){
                            shareViewModel.insert(write)
                        }
                        show()
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerview)
        }

        shareViewModel.authorsQuotes.observe(viewLifecycleOwner) { quotes ->
            adapter.submitList(quotes)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}