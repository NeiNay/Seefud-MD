package com.seefud.seefud.view.content.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.seefud.seefud.data.pref.Vendor
import com.seefud.seefud.databinding.FragmentHomeBinding
import com.seefud.seefud.view.content.VendorAdapter
import com.seefud.seefud.view.content.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var vendorAdapter: VendorAdapter

    private val homeViewModel: HomeViewModel by viewModels {
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

        // Observe session data
//        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
//            if (!user.isLogin) {
//                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
//                requireActivity().finish()
//            }
//        }

        setupSearch()
        setupRecyclerView()
    }

    private fun setupSearch() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()
                searchBar.setText(query)
                searchView.hide()
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun setupRecyclerView() {
        // Sample vendors
        val vendors = listOf(
            Vendor(
                id = "1",
                name = "Vendor A",
                description = "A food vendor specializing in local dishes.",
                imageUrl = ""
            ),
            Vendor(
                id = "2",
                name = "Vendor B",
                description = "A popular vendor offering beverages and snacks.",
                imageUrl = ""
            )
        )

        vendorAdapter = VendorAdapter()
        vendorAdapter.submitList(vendors)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
