package com.view.imgurviewer.models

data class ImgurResponse(
        val data: List<Image>,
        val status: Int,
        val success: Boolean
)