package com.view.imgurviewer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.view.imgurviewer.models.Image

@Database(
        entities = [Image::class],
        version = 1,

)
@TypeConverters(Converters::class)
abstract class ImageDatabase : RoomDatabase(){
    abstract fun getImageDao(): ImageDao


    companion object {
        @Volatile
        private var instance: ImageDatabase? = null
        private val LOCK = Any()

        // function returns current instance of the database, if null then createDatabase is called
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it}
        }
        private fun createDatabase(context: Context) =
                Room.databaseBuilder(
                     context.applicationContext,
                     ImageDatabase::class.java,
                     "image_database.db"
                ).build()
    }
}