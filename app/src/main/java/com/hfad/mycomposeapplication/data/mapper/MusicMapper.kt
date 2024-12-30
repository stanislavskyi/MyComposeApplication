package com.hfad.mycomposeapplication.data.mapper

import com.hfad.mycomposeapplication.data.database.MusicDbModel
import com.hfad.mycomposeapplication.domain.entity.Music
import javax.inject.Inject

class MusicMapper @Inject constructor() {

    fun mapEntityToDbModel(music: Music): MusicDbModel =
        MusicDbModel(
            title = music.title ?: throw IllegalArgumentException("Music title cannot be null"),
            imageLong = music.imageLong,
            uri = music.uri
        )

    fun mapDbModelToEntity(dbModel: MusicDbModel): Music =
        Music(
            title = dbModel.title,
            imageLong = dbModel.imageLong,
            uri = dbModel.uri
        )


}