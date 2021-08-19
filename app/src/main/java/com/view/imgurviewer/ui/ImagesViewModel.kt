package com.view.imgurviewer.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.view.imgurviewer.Resource
import com.view.imgurviewer.models.ImgurResponse
import com.view.imgurviewer.repo.ImagesRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class ImagesViewModel(val imageRepository: ImagesRepo) :ViewModel(){
    val popularImages: MutableLiveData<Resource<ImgurResponse>> = MutableLiveData()     // Live Data used for fragments to subscribe to the live data to automatically update them
    var popularImagesPage = 1

    val searchImages: MutableLiveData<Resource<ImgurResponse>> = MutableLiveData()     // used for fragments to subscribe to the live data to automatically update them
    var searchImagesPage = 1
    init {
        getPopularImages("hot", "viral")            //  To find the true "popular images" I must search by section and sort name as suggested by ImgurAPI
    }
    fun getPopularImages(sectionName: String, sortName: String)= viewModelScope.launch{
        popularImages.postValue(Resource.Loading())
        val response = imageRepository.getPopularImages(sectionName, sortName, popularImagesPage)
        popularImages.postValue(handlePopularImages(response))
    }
    fun searchImages(searchQuery: String) = viewModelScope.launch {
        searchImages.postValue(Resource.Loading())
        val response = imageRepository.searchImages(searchQuery, searchImagesPage)
        searchImages.postValue(handleSearchImages(response))
    }
    private fun handlePopularImages(response: Response<ImgurResponse>): Resource<ImgurResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchImages(response: Response<ImgurResponse>): Resource<ImgurResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}