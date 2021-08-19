package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.view.imgurviewer.R
import com.view.imgurviewer.ui.ImagesViewModel
import com.view.imgurviewer.ui.ImgurActivity
import kotlinx.android.synthetic.main.fragment_image_details.*

class ImageDetailsFragment : Fragment(R.layout.fragment_favourited_images) {
    lateinit var viewModel: ImagesViewModel
    val args: ImageDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImgurActivity).viewModel
        val image = args.image

        fab.setOnClickListener {
            viewModel.favouriteImage(image)
            Snackbar.make(view, "Imaged added to favourites", Snackbar.LENGTH_SHORT).show()
        }
    }
}