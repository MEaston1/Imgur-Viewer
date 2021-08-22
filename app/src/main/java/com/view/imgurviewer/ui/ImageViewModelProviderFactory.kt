package com.view.imgurviewer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.view.imgurviewer.repo.ImagesRepo
// inherits from viewModelProvider to create a new instance of images viewmodel
class ImageViewModelProviderFactory(val imagesRepo: ImagesRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ImagesViewModel(imagesRepo) as T
    }
}