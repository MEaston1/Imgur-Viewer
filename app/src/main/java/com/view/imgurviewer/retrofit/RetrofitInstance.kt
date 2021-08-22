package com.view.imgurviewer.retrofit

import com.view.imgurviewer.retrofit.ImgurConstants.Companion.BASE_URL
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {                  // 'lazy' to only initialise once
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()                         //interceptor used to create network client
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())     //Gson used to determine how the response should be converted to kotlin objects
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(ImgurAPI::class.java)
        }
    }
}