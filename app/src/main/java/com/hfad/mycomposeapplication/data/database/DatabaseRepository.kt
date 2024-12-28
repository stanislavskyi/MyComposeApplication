package com.hfad.mycomposeapplication.data.database

import com.hfad.mycomposeapplication.domain.entity.Audio

interface DatabaseRepository {
    suspend fun insert(music: Audio)
    suspend fun getAll(): List<Audio>
    suspend fun deleteItem(music: Audio)
}