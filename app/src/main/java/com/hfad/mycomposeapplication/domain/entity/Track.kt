package com.hfad.mycomposeapplication.domain.entity

import android.net.Uri

data class Track(
    val id: Long = 0L,
    val title: String = "",
    val duration: Long = 1,
    val link: String = "",
    val preview: String = "",
    val md5_image: String = "",
    val cover_medium: String = "",

    val artist: String = "",
    val uri: Uri? = null
)