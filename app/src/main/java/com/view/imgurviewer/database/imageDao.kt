package com.view.imgurviewer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.view.imgurviewer.models.Image

// image data access object is an interface to define the database access functions (for favourite/liked images)
@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)        // inserts new images, or updates and replaces duplicates
    suspend fun updateOrInsert(image: Image): Long
    @Query("SELECT * FROM images")
    fun getAllImages(): LiveData<List<Image>>               // returns a list of images
    @Delete
    suspend fun unFavourite(image: Image)

}