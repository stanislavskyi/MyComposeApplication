package com.hfad.mycomposeapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicDbModel)

    @Query("SELECT * FROM music")
    suspend fun getAllMusic(): List<MusicDbModel>

    @Query("DELETE FROM music WHERE title=:title")
    suspend fun deleteById(title: String)
}