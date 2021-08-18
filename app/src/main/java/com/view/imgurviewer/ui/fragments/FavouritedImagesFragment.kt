package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.view.imgurviewer.R
import com.view.imgurviewer.ui.ImagesViewModel
import com.view.imgurviewer.ui.ImgurActivity

// A fragment that will display all of the images 'favourited' by the user
class FavouritedImagesFragment : Fragment(R.layout.fragment_favourited_images) {

    lateinit var viewModel: ImagesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImgurActivity).viewModel
    }
}