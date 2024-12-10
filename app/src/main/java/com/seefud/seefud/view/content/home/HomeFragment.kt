package com.seefud.seefud.view.content.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seefud.seefud.R
import com.seefud.seefud.data.Result
import com.seefud.seefud.databinding.FragmentHomeBinding
import com.seefud.seefud.view.content.VendorAdapter
import com.seefud.seefud.view.content.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var vendorAdapter: VendorAdapter
    private lateinit var searchAdapter: VendorAdapter
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
        setupRecyclerView()
        viewModel.fetchVendors()

        viewModel.vendors.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.noDataText.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.noDataText.visibility = View.GONE
                    vendorAdapter.submitList(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.textView5.visibility = View.GONE
                    binding.textView6.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSearch() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchBar.setOnClickListener {
                searchView.show()
            }

            searchAdapter = VendorAdapter()
            searchView.findViewById<RecyclerView>(R.id.rv_search_results).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = searchAdapter
                setHasFixedSize(true)
            }

            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString()
                    if (query.isNotEmpty()) {
                        filterVendors(query)
                    } else {
                        searchAdapter.submitList(emptyList())
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setupRecyclerView() {
        vendorAdapter = VendorAdapter()
        binding.rvHalal.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = vendorAdapter
            setHasFixedSize(true)
        }

        binding.rvMitra.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = vendorAdapter
            setHasFixedSize(true)
        }
    }

    private fun filterVendors(query: String) {
        val filteredList = when (val result = viewModel.vendors.value) {
            is Result.Success -> {
                result.data.filter {
                    it.name?.contains(query, ignoreCase = true) == true || it.description?.contains(
                        query,
                        ignoreCase = true
                    ) == true
                }
            }

            else -> {
                emptyList()
            }
        }
        searchAdapter.submitList(filteredList)

        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "No item found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
