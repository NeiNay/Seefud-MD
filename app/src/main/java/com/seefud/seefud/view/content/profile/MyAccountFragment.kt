package com.seefud.seefud.view.content.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.seefud.seefud.R
import com.seefud.seefud.databinding.FragmentMyAccountBinding
import com.seefud.seefud.view.content.ViewModelFactory

class MyAccountFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var editProfileButton: Button
    private lateinit var editTxtDesc: TextView
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyAccountBinding.inflate(inflater, container, false)

        nameEditText = binding.nameEditText
        emailEditText = binding.emailEditText
        editProfileButton = binding.editProfileButton
        editTxtDesc = binding.pageDesc

        // Get the ViewModelFactory instance (make sure to pass context if needed)
        val viewModelFactory = ViewModelFactory.getInstance(requireContext())

        // Use ViewModelFactory to create ProfileViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        setupEditProfileButton()
        observeUserData()

        return binding.root
    }

    private fun setupEditProfileButton() {
        editProfileButton.setOnClickListener {
            val isEditable = !nameEditText.isEnabled
            nameEditText.isEnabled = isEditable
            emailEditText.isEnabled = isEditable

            if (isEditable) {
                editTxtDesc.text = getString(R.string.save_profile_txt)
                editProfileButton.text = getString(R.string.save_profile)
            } else {
                editTxtDesc.text = getString(R.string.edit_profile_txt)
                editProfileButton.text = getString(R.string.edit_profile)
            }

            if (!isEditable) {
                saveUserData()
            }
        }
    }

    private fun observeUserData() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                nameEditText.setText(user.name)
                emailEditText.setText(user.email)
            }
        }
    }

    private fun saveUserData() {
        val updatedName = nameEditText.text.toString()
        val updatedEmail = emailEditText.text.toString()

        // Call the ViewModel to simulate saving the user data
        viewModel.updateUserProfile(updatedName, updatedEmail)

        // After saving, show a Toast and navigate back to the ProfileFragment
        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()

        // Optionally, navigate back to the ProfileFragment (or previous fragment)
        requireActivity().supportFragmentManager.popBackStack()
    }
}