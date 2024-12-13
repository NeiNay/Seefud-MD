package com.seefud.seefud.view.content.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
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
    private var qrCodeBitmap: Bitmap? = null

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
                viewModel.getVendor().observe(viewLifecycleOwner) { vendor ->
                    vendor?.let {
                        binding.loginSection.visibility = View.GONE
                        binding.userName.text = user.name
                        binding.txtVerified.visibility =
                            if (it.is_verified) View.VISIBLE else View.GONE
                        qrCodeBitmap = generateQRCode(it.id.toString())
                    }
                }
            } else {
                with(binding) {
                    userName.text = getString(R.string.you_haven_t_logged_in_yet)
                    myAccSection.visibility = View.GONE
                    logoutSection.visibility = View.GONE
                    deleteSection.visibility = View.GONE
                    addDataSection.visibility = View.GONE
                    profileImage.visibility = View.GONE
                    txtVerified.visibility = View.GONE
                    qrCodeImage.visibility = View.GONE
                }

            }
        }
    }

    private fun generateQRCode(data: String): Bitmap {
        val size = 512
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    private fun setupAction() {
        // MyAccount Section
        binding.myAccSection.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_myAccountFragment)
        }

        // QR Detail Section
        binding.qrCodeImage.setOnClickListener {
            qrCodeBitmap?.let { qrCode ->
                val bundle = Bundle().apply {
                    putParcelable("qr_code_bitmap", qrCode)
                }
                findNavController().navigate(
                    R.id.action_navigation_profile_to_qrDetailFragment,
                    bundle
                )
            }
        }

        // Login Section
        binding.loginSection.setOnClickListener {
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
        }

        // Add item Section
        binding.addDataSection.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_addDishFragment)
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
