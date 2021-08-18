package com.view.imgurviewer.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "images"
)
data class Image(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val animated: Boolean,
    val bandwidth: Long,
    val datetime: Int,
    val description: Any,
    val height: Int,
    //val id: String,
    val link: String,
    val size: Int,
    val title: Any,
    val type: String,
    val views: Int,
    val width: Int
)