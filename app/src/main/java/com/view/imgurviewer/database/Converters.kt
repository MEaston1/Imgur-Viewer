package com.view.imgurviewer.database

import androidx.room.TypeConverter
import com.view.imgurviewer.models.Data


class Converters {
    @TypeConverter
    fun fromData(data: Data): String{
        return data.name
    }
    @TypeConverter
    fun toData(name: String): Data {
        return Data(name, name)
    }

}