package com.seefud.seefud.view.content.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.seefud.seefud.R
import com.seefud.seefud.databinding.FragmentProfileBinding
import com.seefud.seefud.view.authentication.welcome.WelcomeActivity
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
        observeViewModel()
        setupAction()
        return root
    }

    private fun observeViewModel() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                binding.userName.text = user.name
                binding.loginSection.visibility = View.GONE
            } else {
                binding.userName.text = getString(R.string.you_haven_t_logged_in_yet)
                binding.myAccSection.visibility = View.GONE
                binding.logoutSection.visibility = View.GONE
                binding.deleteSection.visibility = View.GONE
                binding.addDataSection.visibility = View.GONE
            }
        }
    }

    private fun setupAction() {
        // MyAccount Section
        binding.myAccSection.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_myAccountFragment)
        }

        // Login Section
        binding.loginSection.setOnClickListener {
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }

        // Add item Section
        binding.addDataSection.setOnClickListener {
            Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        // Logout Section
        binding.logoutSection.setOnClickListener {
            viewModel.logout()
            Toast.makeText(context, "You've logged out", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
        }

        // Delete Account Section
        binding.deleteSection.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes") { _, _ ->
                    //viewModel.deleteAccount()
                    Toast.makeText(context, "simulating delete Account", Toast.LENGTH_SHORT).show()
                }.setNegativeButton("Cancel", null).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
