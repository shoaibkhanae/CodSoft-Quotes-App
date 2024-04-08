package com.example.quotesapp.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.quotesapp.R
import com.example.quotesapp.data.db.Quote
import com.example.quotesapp.databinding.FragmentQuoteBinding
import com.example.quotesapp.di.QuoteApplication
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import com.example.quotesapp.ui.viewmodels.QuoteViewModelFactory


class QuoteFragment : Fragment() {
    private var _binding: FragmentQuoteBinding? = null
    private val binding
        get() = _binding!!

    private val shareViewModel: QuoteViewModel by activityViewModels {
        QuoteViewModelFactory((requireActivity().application as QuoteApplication).repository)
    }

    lateinit var content: String
    lateinit var author: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            content = QuoteFragmentArgs.fromBundle(it).content
            author = QuoteFragmentArgs.fromBundle(it).author
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_quote,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            quoteFragment = this@QuoteFragment
        }

        binding.copyButton.setOnClickListener { copyQuote() }

        binding.saveButton.setOnClickListener { saveQuote() }

        binding.shareButton.setOnClickListener { shareQuote() }

    }

    private fun saveQuote() {
        val quote = Quote(content = content, author = author)
        shareViewModel.insert(quote)
        Toast.makeText(requireContext(),"Saved Successfully",Toast.LENGTH_SHORT).show()
    }

    private fun shareQuote() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"$content\n$author")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent,null)
        startActivity(shareIntent)
    }

    private fun copyQuote() {
        val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Quote",content + author)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(requireContext(),"copied",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}