package dev.decagon.godday.xkcdcomicsapp.common.data.repository

import dev.decagon.godday.xkcdcomicsapp.common.data.api.SearchComicApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.XkcdComicsApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.model.mapper.ApiComicMapper
import dev.decagon.godday.xkcdcomicsapp.common.data.api.safeApiCall
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.Cache
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.model.FavoriteComic
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class XkcdComicRepository @Inject constructor(
    private val comicApi: XkcdComicsApi,
    private val searchApi: SearchComicApi,
    private val apiComicMapper: ApiComicMapper,
    private val cache: Cache
) : ComicRepository {

    override suspend fun getXkcdComicById(id: Long): XkcdComics? =
        safeApiCall {
            val xkcdComic = comicApi.getXkcdComicById(id) ?: return@safeApiCall null
            apiComicMapper.mapToDomain(xkcdComic)
        }

    override suspend fun searchXkcdComic(query: String): List<XkcdComics> =
        safeApiCall {
            val searchResult = searchApi.searchXkcdComic(query)
            searchResult.map { apiComicMapper.mapToDomain(it) }
        }

    override suspend fun saveFavoriteComic(comics: XkcdComics) {
        cache.storeFavoriteComic(FavoriteComic.fromDomain(comics))
    }

    override fun getFavouriteComics(): Flow<List<XkcdComics>> {
        return cache.getFavoritesComics()
            .distinctUntilChanged()
            .map { favoriteComicsList ->
                favoriteComicsList.map { it.toDomain() }
            }
    }

}