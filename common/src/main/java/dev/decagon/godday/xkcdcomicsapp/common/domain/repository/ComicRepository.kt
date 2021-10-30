package dev.decagon.godday.xkcdcomicsapp.common.domain.repository

import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import kotlinx.coroutines.flow.Flow

interface ComicRepository {
    suspend fun getXkcdComicById(id: Long): XkcdComics?
    suspend fun searchXkcdComic(query: String): List<XkcdComics>

    suspend fun saveFavoriteComic(comics: XkcdComics)
    fun getFavouriteComics(): Flow<List<XkcdComics>>
}
