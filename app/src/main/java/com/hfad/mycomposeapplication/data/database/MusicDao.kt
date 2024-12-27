package com.hfad.mycomposeapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceList(music: MusicDbModel)

    @Query("SELECT * FROM music")
    suspend fun getPriceList(): List<MusicDbModel>
}