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
import androidx.fragment.app.viewModels
import com.seefud.seefud.R
import com.seefud.seefud.databinding.FragmentMyAccountBinding
import com.seefud.seefud.view.content.ViewModelFactory

class MyAccountFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var editProfileButton: Button
    private lateinit var editTxtDesc: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyAccountBinding.inflate(inflater, container, false)

        nameEditText = binding.nameEditText
        emailEditText = binding.emailEditText
        descriptionEditText = binding.descriptionEditText
        editProfileButton = binding.editProfileButton
        editTxtDesc = binding.pageDesc

        setupEditProfileButton()
        observeUserData()

        return binding.root
    }

    private fun setupEditProfileButton() {
        editProfileButton.setOnClickListener {
            val isEditable = !nameEditText.isEnabled
            toggleEditableFields(isEditable)

            if (!isEditable) {
                saveVendorData()
            }
        }
    }

    private fun toggleEditableFields(isEditable: Boolean) {
        nameEditText.isEnabled = isEditable
        emailEditText.isEnabled = isEditable
        descriptionEditText.isEnabled = isEditable

        editTxtDesc.text = if (isEditable) {
            getString(R.string.save_profile_txt)
        } else {
            getString(R.string.edit_profile_txt)
        }

        editProfileButton.text = if (isEditable) {
            getString(R.string.save_profile)
        } else {
            getString(R.string.edit_profile)
        }
    }

    private fun observeUserData() {
        // Observe user data
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                nameEditText.setText(user.name)
                emailEditText.setText(user.email)
            }
        }

        // Observe vendor data
        viewModel.getVendor().observe(viewLifecycleOwner) { vendor ->
            nameEditText.setText(vendor.store_name)
            descriptionEditText.setText(vendor.description)
        }
    }

    private fun saveVendorData() {
        val updatedName = nameEditText.text.toString()
        val updatedDescription = descriptionEditText.text.toString()

        viewModel.getVendor().observe(viewLifecycleOwner) { vendor ->
            val updatedVendor = vendor.copy(
                store_name = updatedName, description = updatedDescription
            )

            viewModel.updateVendor(updatedVendor)

            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
