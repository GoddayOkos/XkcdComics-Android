package dev.decagon.godday.xkcdcomicsapp.browsecomics.domain.usecases

import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import javax.inject.Inject

class AddComicToFavorite @Inject constructor(private val comicRepository: ComicRepository) {
    suspend operator fun invoke(comics: XkcdComics) {
        comicRepository.saveFavoriteComic(comics)
    }
}