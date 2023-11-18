package com.hardcoreamature.chefiq

import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import java.io.ByteArrayOutputStream

object CameraUtils {
    var imageCaptureLauncher: ActivityResultLauncher<Intent>? = null

    fun initialize(activity: FragmentActivity, imageSelectionListener: ImageSelectionListener) {
        imageCaptureLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == FragmentActivity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    val imageString = convertBitmapToString(imageBitmap)
                    imageSelectionListener.onImageSelected(imageString)
                }
            }
        }
    }

    private fun convertBitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun dispatchTakePictureIntent(activity: FragmentActivity) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.CAMERA
        )

        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
            launchCamera(activity)
        } else {
            // Request camera permission
            // You can use the new permission API or any other method to request permission
            // For simplicity, request permission using your preferred method
        }
    }

    private fun launchCamera(activity: FragmentActivity) {
        val takePictureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        imageCaptureLauncher?.launch(takePictureIntent)
    }
}
