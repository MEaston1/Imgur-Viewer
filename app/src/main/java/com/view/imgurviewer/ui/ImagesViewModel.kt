package com.view.imgurviewer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.view.imgurviewer.Resource
import com.view.imgurviewer.models.Image
import com.view.imgurviewer.models.ImgurResponse
import com.view.imgurviewer.repo.ImagesRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class ImagesViewModel(val imageRepository: ImagesRepo) :ViewModel(){
    val popularImages: MutableLiveData<Resource<ImgurResponse>> = MutableLiveData()     // Live Data used for fragments to subscribe to the live data to automatically update them
    var popularImagesPage = 1
    var popularImagesResponse: ImgurResponse? = null

    val searchImages: MutableLiveData<Resource<ImgurResponse>> = MutableLiveData()     // used for fragments to subscribe to the live data to automatically update them
    var searchImagesPage = 1
    var searchImagesResponse: ImgurResponse? = null
    init {
        getPopularImages("hot", "viral")            //  To find the true "popular images" I must search by section and sort name as suggested by ImgurAPI
    }
    fun getPopularImages(sectionName: String, sortName: String)= viewModelScope.launch{
        popularImages.postValue(Resource.Loading())
        val response = imageRepository.getPopularImages(sectionName, sortName, popularImagesPage)
        popularImages.postValue(handlePopularImages(response))
    }
    fun searchImages(searchQuery: String) = viewModelScope.launch {
        searchImages.postValue(Resource.Loading())                                              //posts to search liveData that the response is loading
        val response = imageRepository.searchImages(searchQuery, searchImagesPage)              // fetch response from request
        searchImages.postValue(handleSearchImages(response))
    }
    private fun handlePopularImages(response: Response<ImgurResponse>): Resource<ImgurResponse>{        // Handles response for popular images
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                popularImagesPage++
                if(popularImagesResponse == null){                      // if response is null, set it to result response set to api
                    popularImagesResponse == resultResponse
                } else {
                    val oldImages = popularImagesResponse?.data         // if response is not null, add new articles to the old
                    val newImages = resultResponse.data
                    oldImages?.addAll(newImages)
                }
                return Resource.Success(popularImagesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchImages(response: Response<ImgurResponse>): Resource<ImgurResponse>{     //clone of above function for searching images
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                searchImagesPage++
                if(searchImagesResponse == null){
                    searchImagesResponse == resultResponse
                } else {
                    val oldImages = searchImagesResponse?.data
                    val newImages = resultResponse.data
                    oldImages?.addAll(newImages)
                }
                return Resource.Success(searchImagesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    fun favouriteImage(image: Image) = viewModelScope.launch {
        imageRepository.insertUpdate(image)
    }
    fun getFavouriteImages() = imageRepository.getFavouriteImages()     // no coroutine required instead observe function from fragments

    fun removeFavourite(image: Image) = viewModelScope.launch {
        imageRepository.unfavouriteImage(image)
    }
}