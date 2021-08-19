package com.view.imgurviewer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(                        // reacts to when dragged up or down, returns no functionality
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {    // once an image is swiped on 
                val position = viewHolder.adapterPosition
                val image = imagesAdapter.differ.currentList[position]      //finds current position
                viewModel.removeFavourite(image)                            // invokes function to remove image from favourites
                Snackbar.make(view, "Image removed from favourites", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.favouriteImage(image)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvFavouritedImages)
        }

        viewModel.getFavouriteImages().observe(viewLifecycleOwner, Observer { images ->     // updates list with according new image favourites
            imagesAdapter.differ.submitList(images)
        })
    }
    private fun setupRecyclerView(){
        imagesAdapter = ImageAdapter()
        rvFavouritedImages.apply{
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}