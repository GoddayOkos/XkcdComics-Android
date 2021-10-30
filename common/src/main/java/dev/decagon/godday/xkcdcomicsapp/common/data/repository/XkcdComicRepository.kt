package dev.decagon.godday.xkcdcomicsapp.common.data.repository

import dev.decagon.godday.xkcdcomicsapp.common.data.api.SearchComicApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.XkcdComicsApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.model.mapper.ApiComicMapper
import dev.decagon.godday.xkcdcomicsapp.common.data.api.safeApiCall
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class XkcdComicRepository @Inject constructor(
    private val comicApi: XkcdComicsApi,
    private val searchApi: SearchComicApi,
    private val apiComicMapper: ApiComicMapper
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
        TODO("Not yet implemented")
    }

    override fun getFavouriteComics(): Flow<List<XkcdComics>> {
        TODO("Not yet implemented")
    }

}