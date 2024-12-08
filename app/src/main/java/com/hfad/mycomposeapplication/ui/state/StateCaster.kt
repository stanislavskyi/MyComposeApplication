package com.hfad.mycomposeapplication.ui.state

import com.hfad.mycomposeapplication.domain.entity.Track

//data class StateCaster(
//        val isPlaying: Boolean,
//        val preview: String = "",
//        val obj: Track = Track(),
//        val currentPosition: Long = 0L, // Текущая позиция воспроизведения в миллисекундах
//        val duration: Long = 0L // Общая продолжительность трека в миллисекундах
//    )

data class StateCaster(
        val isPlaying: Boolean,
        val preview: String = "",
        val obj: Track = Track(),
        val currentPosition: Long = 0L, // Текущая позиция воспроизведения в миллисекундах
        val duration: Long = 0L // Общая продолжительность трека в миллисекундах
)

//sealed class CasterState {
//        object Idle : CasterState() // Ожидание или ничего не играет
//        object Play: CasterState()
//
//        object Paused : CasterState() // Воспроизведение приостановлено
//}