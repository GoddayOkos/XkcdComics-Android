package dev.decagon.godday.xkcdcomicsapp.browsecomics.domain.usecases

import dev.decagon.godday.xkcdcomicsapp.common.domain.NoMoreXkcdComicsException
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import javax.inject.Inject

class GetXkcdComic @Inject constructor(private val comicRepository: ComicRepository) {
    suspend operator fun invoke(id: Long): XkcdComics {
        return comicRepository.getXkcdComicById(id)
            ?: throw NoMoreXkcdComicsException("No more xkcd comics to display")
    }
}