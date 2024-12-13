package com.seefud.seefud.view.content.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seefud.seefud.databinding.FragmentQrDetailBinding

class QrDetailFragment : Fragment() {

    private lateinit var binding: FragmentQrDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrDetailBinding.inflate(inflater, container, false)

        // Get QR code bitmap passed as an argument
        val qrCodeBitmap = requireArguments().getParcelable<Bitmap>("qr_code_bitmap")
        binding.qrCodeFullImage.setImageBitmap(qrCodeBitmap)

        return binding.root
    }
}
