package com.seefud.seefud.view.content.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.seefud.seefud.databinding.FragmentProfileBinding
import com.seefud.seefud.view.content.ViewModelFactory

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListeners()
        return root
    }

    private fun setupListeners() {
        // Logout Section
        binding.logoutSection.setOnClickListener {
            viewModel.logout()
            Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
        }

        // Delete Account Section
        binding.deleteSection.setOnClickListener {
            // Confirm deletion
            AlertDialog.Builder(requireContext()).setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes") { _, _ ->
                    //viewModel.deleteAccount()
                    Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                }.setNegativeButton("Cancel", null).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
