package com.hardcoreamature.chefiq

interface ImageSelectionListener {
    fun onImageSelected(imagePath: String)
    fun onImageSelectionError(errorMessage: String)
}