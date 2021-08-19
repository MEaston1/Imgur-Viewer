package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.view.imgurviewer.ImageAdapter
import com.view.imgurviewer.R
import com.view.imgurviewer.Resource
import com.view.imgurviewer.ui.ImagesViewModel
import com.view.imgurviewer.ui.ImgurActivity
import kotlinx.android.synthetic.main.fragment_popular_images.*

class PopularImagesFragment : Fragment(R.layout.fragment_popular_images) {

    lateinit var viewModel: ImagesViewModel
    lateinit var imagesAdapter: ImageAdapter
    val TAG = "PopularImagesFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImgurActivity).viewModel
        setupRecyclerView()

        imagesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("image", it)
            }
            findNavController().navigate(
                    R.id.action_popularImagesFragment_to_imageDetailsFragment,
                    bundle
            )
        }

        viewModel.popularImages.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { imagesResponse ->
                    imagesAdapter.differ.submitList(imagesResponse.data)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar( ){
        paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        imagesAdapter = ImageAdapter()
        rvPopularImages.apply{
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}