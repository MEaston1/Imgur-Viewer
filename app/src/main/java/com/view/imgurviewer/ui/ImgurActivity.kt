package com.view.imgurviewer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.view.imgurviewer.R
import com.view.imgurviewer.database.ImageDatabase
import com.view.imgurviewer.repo.ImagesRepo
import kotlinx.android.synthetic.main.activity_imgur.*

class ImgurActivity : AppCompatActivity() {
    lateinit var  viewModel: ImagesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgur)
        val ImagesRepository = ImagesRepo(ImageDatabase(this))
        val viewModelProviderFactory = ImageViewModelProviderFactory(ImagesRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ImagesViewModel::class.java)
        bottomNavigationView.setupWithNavController(imgurViewerNavFragment.findNavController())
    }
}