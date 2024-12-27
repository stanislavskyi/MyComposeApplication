package com.hfad.mycomposeapplication.data.database

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class MusicDbModel(
    //@PrimaryKey(autoGenerate = true) val id: Int,
    @PrimaryKey
    val title: String,
    val imageLong: Bitmap? = null,
    val uri: Uri? = null
)