package com.view.imgurviewer.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.view.imgurviewer.database.ImageDao
import com.view.imgurviewer.database.ImageDatabase
import com.view.imgurviewer.getOrAwaitValue
import com.view.imgurviewer.models.Image
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest      // labels unit test
class ImageDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var  database: ImageDatabase
    private lateinit var dao: ImageDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(                    // to avoid setting up a new database for each test case
            ApplicationProvider.getApplicationContext(),
            ImageDatabase::class.java
        ).allowMainThreadQueries().build()                          // to avoid threads manipulating each other
        dao = database.getImageDao()
    }

    @After
    fun teardown(){
        database.close()
    }
    @Test
    fun updateOrInsertImage() = runBlockingTest {
        val ImageItem = Image(2335, false, 1231455, 220321, "This is my test image.", 21, "www.image.com", 24, "Test Image", "image", 325, 24)
        dao.updateOrInsert(ImageItem)

        val allImages = dao.getAllImages().getOrAwaitValue()
        assertThat(allImages).contains(ImageItem)
    }
    @Test                       // Testing to make sure removing an image from the favourites list works
    fun unfavouriteImage() = runBlockingTest{
        val ImageItem = Image(2335, false, 1231455, 220321, "This is my test image.", 21, "www.image.com", 24, "Test Image", "image", 325, 24)
        dao.unFavourite(ImageItem)
        val allImages = dao.getAllImages().getOrAwaitValue()
        assertThat(allImages).doesNotContain(ImageItem)
    }
}