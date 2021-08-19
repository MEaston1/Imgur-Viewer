package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.view.imgurviewer.ImageAdapter
import com.view.imgurviewer.R
import com.view.imgurviewer.ui.ImagesViewModel
import com.view.imgurviewer.ui.ImgurActivity
import kotlinx.android.synthetic.main.fragment_favourited_images.*
import kotlinx.android.synthetic.main.fragment_popular_images.*

// A fragment that will display all of the images 'favourited' by the user
class FavouritedImagesFragment : Fragment(R.layout.fragment_favourited_images) {

    lateinit var viewModel: ImagesViewModel
    lateinit var imagesAdapter: ImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImgurActivity).viewModel
        setupRecyclerView()

        imagesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("image", it)
            }
            findNavController().navigate(
                    R.id.action_favouritedImagesFragment_to_imageDetailsFragment,
                    bundle
            )
        }
    }
    private fun setupRecyclerView(){
        imagesAdapter = ImageAdapter()
        rvFavouritedImages.apply{
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}