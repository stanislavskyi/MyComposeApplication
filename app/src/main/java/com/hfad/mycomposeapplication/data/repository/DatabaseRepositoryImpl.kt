package com.hfad.mycomposeapplication.data.repository

import android.util.Log
import com.hfad.mycomposeapplication.data.database.MusicDao
import com.hfad.mycomposeapplication.data.mapper.MusicMapper
import com.hfad.mycomposeapplication.domain.entity.Music
import com.hfad.mycomposeapplication.domain.repository.DatabaseRepository
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val dao: MusicDao,
    private val musicMapper: MusicMapper
): DatabaseRepository {

    override suspend fun insert(music: Music) {
        val dbModel = musicMapper.mapEntityToDbModel(music)
        try {
            dao.insertMusic(dbModel)
        } catch (e: Exception){
            Log.e("DatabaseRepository", "Error inserting music: ${e.message}", e)
            throw DatabaseException("Error inserting music")
        }
    }

//    override suspend fun getAll(): List<Audio> = dao.getAllMusic().map {
//        Audio(
//            title = it.title,
//            imageLong = it.imageLong,
//            uri = it.uri
//        )
//    }

    override suspend fun getAll(): List<Music> {
        return try {
            dao.getAllMusic().map {
                musicMapper.mapDbModelToEntity(it)
            }
        } catch (e: Exception) {
            Log.e("DatabaseRepository", "Error fetching all music: ${e.message}", e)
            throw DatabaseException("Error fetching all music")
        }
    }

    override suspend fun deleteItem(music: Music) {
        music.title?.let { title ->
            try {
                dao.deleteById(title)
            } catch (e: Exception){
                Log.e("DatabaseRepository", "Error deleting music: ${e.message}", e)
                throw DatabaseException("Error deleting music")
            }
        }

    }

//    private fun mapEntityToDbModel(audio: Audio): MusicDbModel {
//        return MusicDbModel(
//            title = audio.title ?: throw IllegalArgumentException("Music title cannot be null"),
//            imageLong = audio.imageLong,
//            uri = audio.uri
//        )
//    }
//
//    private fun MusicDbModel.toAudio(): Audio {
//        return Audio(
//            title = this.title,
//            imageLong = this.imageLong,
//            uri = this.uri
//        )
//    }

    class DatabaseException(message: String) : Exception(message)
}