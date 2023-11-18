package com.hardcoreamature.chefiq

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hardcoreamature.chefiq.databinding.FragmentCameraBinding
import java.io.File
import android.net.Uri
import java.util.Date
import java.util.Locale

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageSelectionListener: ImageSelectionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize CameraX
        setupCamera()

        // Set up click listener for capturing an image
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun setupCamera() {
        imageCapture = view?.display?.let {
            ImageCapture.Builder()
                .setTargetRotation(it.rotation)
                .build()
        }!!

        // Additional CameraX initialization if needed
    }

    private fun takePhoto() {
        val photoFile = createPhotoFile()

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    imageSelectionListener.onImageSelected(savedUri.toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    imageSelectionListener.onImageSelectionError(
                        exception.message ?: "Unknown error"
                    )
                }
            }
        )
    }

    private fun createPhotoFile(): File {
        // Get the external storage directory
        val storageDirectory = File(requireContext().getExternalFilesDir(null), "photos")

        // Create the directory if it doesn't exist
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs()
        }

        // Create a unique file for each photo
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFileName = "JPEG_${timeStamp}_"
        val photoFile = File.createTempFile(
            photoFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDirectory /* directory */
        )

        return photoFile
    }

    fun onCaptureButtonClick(view: View) {}
}
