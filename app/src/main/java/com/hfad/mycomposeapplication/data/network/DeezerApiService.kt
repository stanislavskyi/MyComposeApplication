package com.hfad.mycomposeapplication.data.network

import com.hfad.mycomposeapplication.data.network.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface DeezerApiService {
    @GET("playlist/3155776842/tracks")
    suspend fun getTracks(
        @Query("index") index: Int,
        @Query("limit") limit: Int
    ): TrackResponse
}