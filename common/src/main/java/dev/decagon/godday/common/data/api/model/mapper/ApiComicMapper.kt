package dev.decagon.godday.common.data.api.model.mapper

import dev.decagon.godday.common.data.api.model.XkcdComicDTO
import dev.decagon.godday.common.domain.model.XkcdComics
import javax.inject.Inject

class ApiComicMapper @Inject constructor(): ApiMapper<XkcdComicDTO, XkcdComics> {
    override fun mapToDomain(apiDTO: XkcdComicDTO): XkcdComics {
        return XkcdComics(
            id = apiDTO.id,
            day = apiDTO.day,
            month = apiDTO.month,
            title = apiDTO.title,
            year = apiDTO.year,
            imageUrl = apiDTO.imageUrl,
            shortDescription = apiDTO.subText,
            _description = apiDTO.description ?: ""
        )
    }
}