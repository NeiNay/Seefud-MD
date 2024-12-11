package com.seefud.seefud.view.content.profile.addDish

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.seefud.seefud.data.Result
import com.seefud.seefud.databinding.FragmentAddDishBinding
import com.seefud.seefud.view.content.ViewModelFactory
import java.io.File

class AddDishFragment : Fragment() {

    private lateinit var binding: FragmentAddDishBinding
    private val viewModel by viewModels<AddDishViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDishBinding.inflate(inflater, container, false)

        setupUI()
        observeViewModel()

        return binding.root
    }

    private fun setupUI() {
        binding.uploadImageButton.setOnClickListener {
            openGallery()
        }

        binding.saveButton.setOnClickListener {
            saveDish()
        }
    }

    private fun observeViewModel() {
        viewModel.createProductResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Dish added successfully", Toast.LENGTH_SHORT).show()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openGallery() {
        val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.dishImg.setImageURI(it)
            }
        }
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun saveDish() {
        val name = binding.dishNameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()

        if (name.isNotEmpty() && description.isNotEmpty()) {
            // Convert the Uri to File before passing to the ViewModel
            val imageFile = selectedImageUri?.let { getFileFromUri(it) }

            viewModel.saveDish(name, description, imageFile)
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val cursor = context?.contentResolver?.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        return if (filePath != null) {
            File(filePath)
        } else {
            null
        }
    }
}
