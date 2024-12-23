package com.seefud.seefud.view.content.scan

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.seefud.seefud.R
import com.seefud.seefud.databinding.FragmentScanBinding
import com.seefud.seefud.view.content.detail.DetailActivity

class ScanFragment : Fragment() {

    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var binding: FragmentScanBinding
    private var firstCall = true
    private lateinit var viewModel: ScanViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                findNavController().navigate(R.id.navigation_home)
                showPermissionRequiredDialog()
            }
        }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireContext(), REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ScanViewModel::class.java]
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
        val options =
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        barcodeScanner = BarcodeScanning.getClient(options)

        val analyzer = MlKitAnalyzer(
            listOf(barcodeScanner),
            COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(requireContext())
        ) { result: MlKitAnalyzer.Result? ->
            showResult(result)
        }

        val cameraController = LifecycleCameraController(requireContext()).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_ANALYSIS
            )
        }
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(requireContext()), analyzer
        )
        cameraController.bindToLifecycle(viewLifecycleOwner)
        binding.viewFinder.controller = cameraController
    }

    private fun showResult(result: MlKitAnalyzer.Result?) {
        if (firstCall) {
            val barcodeResults = result?.getValue(barcodeScanner)
            if ((barcodeResults != null) && (barcodeResults.size != 0) && (barcodeResults.first() != null)) {
                firstCall = false
                val barcode = barcodeResults[0]
                showAlertDialog(barcode)
            }
        }
    }

    private fun showAlertDialog(barcode: Barcode) {
        if (barcode.rawValue != null) {
            viewModel.setScannedId(barcode.rawValue!!)

            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("scannedId", barcode.rawValue)
            startActivity(intent)
        }
    }

    private fun showPermissionRequiredDialog() {
        AlertDialog.Builder(requireContext()).setTitle("Permission was Denied")
            .setMessage("Camera access is needed to scan QR codes. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                }
                startActivity(intent)
            }.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }.setCancelable(false).show()
    }

    private fun hideSystemUI() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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
