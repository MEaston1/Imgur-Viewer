package com.view.imgurviewer.models

data class ImgurResponse(
        val data: MutableList<Image>,
        val status: Int,
        val success: Boolean,
        val totalResults: Int
)