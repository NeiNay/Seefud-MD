package com.seefud.seefud.view.content.dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.seefud.seefud.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var binding: FragmentDashboardBinding
    private var firstCall = true

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun startCamera() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        val analyzer = MlKitAnalyzer(
            listOf(barcodeScanner),
            COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(requireContext())
        ) { result: MlKitAnalyzer.Result? ->
            showResult(result)
        }

        val cameraController = LifecycleCameraController(requireContext())
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(requireContext()),
            analyzer
        )
        cameraController.bindToLifecycle(viewLifecycleOwner)
        binding.viewFinder.controller = cameraController
    }

    private fun showResult(result: MlKitAnalyzer.Result?) {
        if (firstCall) {
            val barcodeResults = result?.getValue(barcodeScanner)
            if (!barcodeResults.isNullOrEmpty()) {
                firstCall = false
                val barcode = barcodeResults[0]
                showAlertDialog(barcode)
            }
        }
    }

    private fun showAlertDialog(barcode: Barcode) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setMessage(barcode.rawValue)
            .setPositiveButton("Buka") { _, _ ->
                firstCall = true
                when (barcode.valueType) {
                    Barcode.TYPE_URL -> {
                        val openBrowserIntent = Intent(Intent.ACTION_VIEW)
                        openBrowserIntent.data = Uri.parse(barcode.url?.url)
                        startActivity(openBrowserIntent)
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Unsupported data type", Toast.LENGTH_SHORT).show()
                        startCamera()
                    }
                }
            }
            .setNegativeButton("Scan lagi") { _, _ -> firstCall = true }
            .setCancelable(false)
            .create()
        alertDialog.show()
    }

    private fun hideSystemUI() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        activity?.actionBar?.hide()
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}