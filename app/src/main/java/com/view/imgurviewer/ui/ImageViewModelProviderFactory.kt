package com.view.imgurviewer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.view.imgurviewer.repo.ImagesRepo

class ImageViewModelProviderFactory(val imagesRepo: ImagesRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImagesViewModel(imagesRepo) as T
    }
}