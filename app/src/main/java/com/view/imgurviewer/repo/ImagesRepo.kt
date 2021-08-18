package com.view.imgurviewer.repo

import com.view.imgurviewer.database.ImageDatabase
import com.view.imgurviewer.retrofit.RetrofitInstance

class ImagesRepo(
        val db: ImageDatabase
){
    suspend fun getPopularImages(sectionName: String, sortName: String, pageNumber: Int) =
            RetrofitInstance.api.getGallery(sectionName, sortName, pageNumber)

    suspend fun searchImages(searchQuery: String, pageNumber: Int) =
            RetrofitInstance.api.searchForImages(searchQuery, pageNumber)
}