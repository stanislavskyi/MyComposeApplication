package com.hfad.mycomposeapplication.domain.entity

import android.graphics.Bitmap
import android.net.Uri

data class Music(
    val title: String? = null,
    var imageLong: Bitmap? = null,
    val uri: Uri? = null,
    val isOptionsRevealed: Boolean = false
)