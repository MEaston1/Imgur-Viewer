package com.view.imgurviewer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.view.imgurviewer.R
import kotlinx.android.synthetic.main.activity_imgur.*

class ImgurActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgur)

        bottomNavigationView.setupWithNavController(imgurViewerNavFragment.findNavController())
    }
}