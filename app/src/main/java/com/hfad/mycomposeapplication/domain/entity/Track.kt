package com.hfad.mycomposeapplication.domain.entity

import android.net.Uri

data class Track(
    val id: Long = 1,
    val title: String = "asdasd",
    val duration: Long = 1,
    val link: String = "123123",
    val preview: String = "123123",
    val md5_image: String = "123123",
    val cover_medium: String = "124124",

    val artist: String = " 124214124",
    val uri: Uri? = null
)