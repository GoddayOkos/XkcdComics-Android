package dev.decagon.godday.xkcdcomicsapp.favorites.domain.usecases

import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import javax.inject.Inject

class GetFavoriteComicsFromDB @Inject constructor(
    private val comicRepository : ComicRepository
) {
    operator fun invoke() = comicRepository.getFavouriteComics()
}