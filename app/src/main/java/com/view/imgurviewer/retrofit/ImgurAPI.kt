package com.view.imgurviewer.retrofit

import com.view.imgurviewer.models.ImgurResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
// the elements of my network requests
interface ImgurAPI {
    @GET("gallery")
    suspend fun getGallery(
            @Query("section")
            sectionName: String = "hot",
            @Query("sort")
            sortName: String = "viral",
            @Query("page")      // page value to allow for pagination
            pageNumber: Int = 1,
            @Query("apiKey")
            apiKey: String = ApiKeys.CLIENT_ID
    ): Response<ImgurResponse>

    // separate network request for the search feature
    @GET("gallery")
    suspend fun searchForImages(
        @Query("q")
        searchName: String,
        @Query("page")      // page value to allow for pagination
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = ApiKeys.CLIENT_ID
    ): Response<ImgurResponse>

    // the api key required for access to ImgurAPI (yes i know you shouldn't normally make the api key visible)
    object ApiKeys{
        const val CLIENT_ID = "3ab1b23f1494a2c"
    }
}