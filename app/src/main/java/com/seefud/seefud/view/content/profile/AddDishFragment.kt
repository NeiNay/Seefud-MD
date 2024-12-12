package com.seefud.seefud.view.content.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.seefud.seefud.data.Result
import com.seefud.seefud.databinding.FragmentAddDishBinding
import com.seefud.seefud.view.content.ViewModelFactory
import com.seefud.seefud.view.content.reduceFileImage
import com.seefud.seefud.view.content.uriToFile

class AddDishFragment : Fragment() {

    private lateinit var binding: FragmentAddDishBinding
    private val viewModel by viewModels<ProfileViewModel> {
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
            startGallery()
        }

        binding.saveButton.setOnClickListener {
            uploadDish()
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

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            selectedImageUri = viewModel.currentImageUri
            binding.dishImg.setImageURI(uri)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadDish() {
        selectedImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            val dishName = binding.dishNameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if (dishName.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            viewModel.uploadDish(dishName, description, imageFile)

            viewModel.createProductResponse.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Dish added successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp() // Navigate back or reset UI after success
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Error: ${result.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } ?: run {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT)
                .show()
        }
    }

//    private fun fetchIngredientsFromAPI() {
//        // Make an API request to get the ingredients
//        val token = "Bearer <your_token>"
//        val apiService = ApiService.create()  // Replace with your actual service creation
//
//        // Call the ingredients endpoint
//        apiService.getIngredients(token).enqueue(object : Callback<IngredientsResponse> {
//            override fun onResponse(call: Call<IngredientsResponse>, response: Response<IngredientsResponse>) {
//                if (response.isSuccessful) {
//                    val ingredients = response.body()?.data?.joinToString(", ") { it.name }
//                    // Set the ingredients in the EditText
//                    binding.ingredientsEditText.setText(ingredients)
//                } else {
//                    Toast.makeText(context, "Failed to fetch ingredients", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<IngredientsResponse>, t: Throwable) {
//                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

}
