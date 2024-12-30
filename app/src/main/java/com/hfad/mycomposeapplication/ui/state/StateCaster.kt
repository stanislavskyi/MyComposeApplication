package com.hfad.mycomposeapplication.ui.state

import android.graphics.Bitmap
import com.hfad.mycomposeapplication.domain.entity.Music
import com.hfad.mycomposeapplication.domain.entity.Track


data class StateCaster(
        val isPlaying: Boolean,
        val preview: String = "",
        val obj: Track = Track(),
        val currentPosition: Long = 0L, // Текущая позиция воспроизведения в миллисекундах
        val duration: Long = 0L, // Общая продолжительность трека в миллисекундах

        //
        val audio: Bitmap? = null,
        val isContent: Boolean = false,


        val musicList: List<Music> = emptyList()
)

sealed class CasterState{
        object Idle: CasterState()
        object Play: CasterState()
        object Paused: CasterState()
        data class Content(
                val isPlaying: Boolean,
                val preview: String = "",
                val obj: Track = Track(),
                val currentPosition: Long = 0L, // Текущая позиция воспроизведения в миллисекундах
                val duration: Long = 0L, // Общая продолжительность трека в миллисекундах
                val audio: Bitmap? = null,
                val isContent: Boolean = false
        )
}
