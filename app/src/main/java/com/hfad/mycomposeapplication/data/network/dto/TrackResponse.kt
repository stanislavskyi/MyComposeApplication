package com.hfad.mycomposeapplication.data.network.dto

data class TrackResponse(
    val data: List<TrackDto>,
    val total: Int
)