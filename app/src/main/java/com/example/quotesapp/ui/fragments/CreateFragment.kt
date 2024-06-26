package com.example.quotesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.data.db.entities.Write
import com.example.quotesapp.databinding.FragmentCreateBinding
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding
        get() = _binding!!

    private val shareViewModel: QuoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createFragment = this@CreateFragment
    }


    fun createQuotes() {
        if (binding.etContent.text.isEmpty()) {
            setupError(true)
        } else {
            setupError(false)

            val content = binding.etContent.text.toString()
            val author = if (binding.etAuthor.text.isEmpty()) {
                "Unknown"
            } else {
                binding.etAuthor.text.toString()
            }

            val write = Write(content = content, author = author)
            shareViewModel.insert(write)

            Toast.makeText(requireContext(),"Quotes Created",Toast.LENGTH_SHORT).show()
            goToIdeasScreen()
        }
    }

    private fun setupError(error: Boolean) {
        if (error) {
            binding.etContent.error = "Content is empty"
        } else {
            binding.etContent.error = null
        }
    }

    private fun goToIdeasScreen() {
        findNavController().navigate(R.id.action_createFragment_to_ideasFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}