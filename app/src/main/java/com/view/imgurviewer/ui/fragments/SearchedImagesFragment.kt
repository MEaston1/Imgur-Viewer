package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.view.imgurviewer.ImageAdapter
import com.view.imgurviewer.R
import com.view.imgurviewer.Resource
import com.view.imgurviewer.ui.ImagesViewModel
import com.view.imgurviewer.ui.ImgurActivity
import com.view.imgurviewer.utilities.Constants
import com.view.imgurviewer.utilities.Constants.Companion.IMG_SEARCH_DELAY
import kotlinx.android.synthetic.main.fragment_popular_images.*
import kotlinx.android.synthetic.main.fragment_searched_images.*
import kotlinx.android.synthetic.main.fragment_searched_images.paginationProgressBar
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

        var job: Job? = null                            // function to add a slight delay from starting  to prevent too many requests
        etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(IMG_SEARCH_DELAY)
                if(editable.toString().isNotEmpty()){                   // to only search for news if there is text
                    viewModel.searchImages(editable.toString())
                }
            }
        }

        viewModel.searchImages.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { imagesResponse ->
                        imagesAdapter.differ.submitList(imagesResponse.data.toList())
                        val totalPages = imagesResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2      // 2 added as integer division is rounded off + last page will be empty
                        isLastPage = viewModel.searchImagesPage == totalPages
                        if(isLastPage){
                            rvPopularImages.setPadding(0,0,0,0)
                        }
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
        isLoading = false
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndLastPage = !isLoading && isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.searchImages(etSearch.text.toString())      // whenever called a request will be sent to
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView(){
        imagesAdapter = ImageAdapter()
        rvSearchImages.apply{
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchedImagesFragment.scrollListener)
        }

    }
}