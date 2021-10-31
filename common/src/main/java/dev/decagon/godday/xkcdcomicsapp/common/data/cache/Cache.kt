package dev.decagon.godday.xkcdcomicsapp.common.data.cache

import dev.decagon.godday.xkcdcomicsapp.common.data.cache.model.FavoriteComic
import kotlinx.coroutines.flow.Flow

interface Cache {
    suspend fun storeFavoriteComic(favoriteComic: FavoriteComic)
    fun getFavoritesComics(): Flow<List<FavoriteComic>>
}