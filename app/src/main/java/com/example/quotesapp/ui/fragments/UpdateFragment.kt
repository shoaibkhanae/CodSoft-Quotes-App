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
import com.example.quotesapp.data.model.entities.Write
import com.example.quotesapp.databinding.FragmentUpdateBinding
import com.example.quotesapp.ui.viewmodels.QuoteViewModel


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    val binding
        get() = _binding!!

    private val shareViewModel: QuoteViewModel by activityViewModels()

    private var id: Int = 0
    private lateinit var author: String
    private lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = UpdateFragmentArgs.fromBundle(it).id
            author = UpdateFragmentArgs.fromBundle(it).author
            content = UpdateFragmentArgs.fromBundle(it).content
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etUpdateAuthor.setText(author)
        binding.etUpdateContent.setText(content)

        binding.updateButton.setOnClickListener { updateQuote() }
    }

    private fun updateQuote() {
        val author = binding.etUpdateAuthor.text.toString()
        val content = binding.etUpdateContent.text.toString()

        val write = Write(id,author,content)
        shareViewModel.insert(write)
        goToIdeasScreen()
        Toast.makeText(requireContext(),"Quote Updated.",Toast.LENGTH_SHORT).show()
    }

    private fun goToIdeasScreen() {
        findNavController().navigate(R.id.action_updateFragment_to_ideasFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}