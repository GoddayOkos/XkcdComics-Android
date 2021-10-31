package dev.decagon.godday.xkcdcomicsapp.common.data.cache

import dev.decagon.godday.xkcdcomicsapp.common.data.cache.dao.FavoriteComicsDao
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.model.FavoriteComic
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val favoriteComicsDao: FavoriteComicsDao
) : Cache {

    override suspend fun storeFavoriteComic(favoriteComic: FavoriteComic) {
        favoriteComicsDao.insertFavoriteComic(favoriteComic)
    }

    override fun getFavoritesComics(): Flow<List<FavoriteComic>> =
        favoriteComicsDao.getFavoriteComics()
}