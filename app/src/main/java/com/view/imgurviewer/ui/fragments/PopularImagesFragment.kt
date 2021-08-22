package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.view.imgurviewer.ImageAdapter
import com.view.imgurviewer.R
import com.view.imgurviewer.Resource
import com.view.imgurviewer.ui.ImagesViewModel
import com.view.imgurviewer.ui.ImgurActivity
import com.view.imgurviewer.utilities.Constants.Companion.QUERY_PAGE_SIZE
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
                    imagesAdapter.differ.submitList(imagesResponse.data.toList())
                        val totalPages = imagesResponse.totalResults / QUERY_PAGE_SIZE + 2      // 2 added as integer division is rounded off + last page will be empty
                        isLastPage = viewModel.popularImagesPage == totalPages
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
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {              // if scrolling, boolean set to true
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()         // three numbers to check if scrolled to the bottom
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            
            val isNotLoadingAndLastPage = !isLoading && isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount        // calculating the last item
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling       // to determine whether to paginate or not
            if(shouldPaginate){
                viewModel.getPopularImages("hot", "viral")      // whenever called a request will be sent for more images of hot and viral
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView(){
        imagesAdapter = ImageAdapter()
        rvPopularImages.apply{
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@PopularImagesFragment.scrollListener)
        }

    }
}