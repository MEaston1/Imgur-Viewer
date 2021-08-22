package com.view.imgurviewer.database

import androidx.room.TypeConverter
import com.view.imgurviewer.models.Data


class Converters {
    @TypeConverter      // to let Room know it's a converter function
    fun fromData(data: Data): String{                   // converting from Data to String
        return data.name
    }
    @TypeConverter
    fun toData(name: String): Data {                    // converting from String to Data
        return Data(name, name)
    }

}