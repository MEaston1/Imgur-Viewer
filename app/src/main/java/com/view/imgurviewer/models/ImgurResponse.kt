package com.view.imgurviewer.models

data class ImgurResponse(
        val `data`: Data,
        val status: Int,
        val success: Boolean
)