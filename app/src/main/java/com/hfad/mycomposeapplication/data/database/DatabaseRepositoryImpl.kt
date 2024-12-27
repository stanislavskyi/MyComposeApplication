package com.hfad.mycomposeapplication.data.database

import com.hfad.mycomposeapplication.domain.entity.Audio
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val dao: MusicDao,
): DatabaseRepository {

    override suspend fun insert(music: Audio) {
        dao.insertPriceList(mapEntityToDbModel(music))
    }

    override suspend fun getAll(): List<Audio> = dao.getPriceList().map {
        Audio(
            title = it.title,
            imageLong = it.imageLong,
            uri = it.uri
        )
    }

    private fun mapEntityToDbModel(audio: Audio): MusicDbModel{
        return MusicDbModel(
            title = audio.title ?: "null",
            imageLong = audio.imageLong,
            uri = audio.uri
        )
    }
}