package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.view.imgurviewer.ImageAdapter
import com.view.imgurviewer.R
import com.view.imgurviewer.Resource
import com.view.imgurviewer.ui.ImagesViewModel
import com.view.imgurviewer.ui.ImgurActivity
import com.view.imgurviewer.utilities.Constants.Companion.IMG_SEARCH_DELAY
import kotlinx.android.synthetic.main.fragment_searched_images.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchedImagesFragment : Fragment(R.layout.fragment_searched_images) {
    lateinit var viewModel: ImagesViewModel
    lateinit var  imagesAdapter: ImageAdapter
    val TAG = "SearchImages"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImgurActivity).viewModel
        setupRecyclerView()

        imagesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("image", it)
            }
            findNavController().navigate(
                    R.id.action_searchedImagesFragment_to_imageDetailsFragment,
                    bundle
            )
        }

        var job: Job? = null
        etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(IMG_SEARCH_DELAY)
                if(editable.toString().isNotEmpty()){
                    viewModel.searchImages(editable.toString())
                }
            }
        }

        viewModel.searchImages.observe(viewLifecycleOwner, Observer { response ->
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
        rvSearchImages.apply{
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}