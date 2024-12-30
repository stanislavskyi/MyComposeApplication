package com.hfad.mycomposeapplication.domain.repository

import com.hfad.mycomposeapplication.domain.entity.Music

interface DatabaseRepository {
    suspend fun insert(music: Music)
    suspend fun getAll(): List<Music>
    suspend fun deleteItem(music: Music)
}