package com.hfad.mycomposeapplication.data.mapper

import com.hfad.mycomposeapplication.data.network.dto.TrackDto
import com.hfad.mycomposeapplication.domain.entity.Track
import javax.inject.Inject

class TrackMapper @Inject constructor() {

    fun mapDtoToEntity(dto: TrackDto) = Track(
        id = dto.id,
        title = dto.title,
        duration = dto.duration.toLong(),
        link = dto.link,
        preview = dto.preview,
        md5_image = dto.md5_image,
        cover_medium = dto.album.cover_xl,
        artist = dto.artist.name
    )

}