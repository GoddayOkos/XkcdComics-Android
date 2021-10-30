package dev.decagon.godday.common.data.api

import dev.decagon.godday.common.data.api.model.XkcdComicDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface XkcdComicsApi {
    @GET(ApiConstants.XKCD_COMIC_ENDPOINT)
    suspend fun getXkcdComicById(@Path("comic_id") comicId: Long): XkcdComicDTO?
}

interface SearchComicApi {
    @GET(ApiConstants.SEARCH_ENDPOINT)
    suspend fun searchXkcdComic(@Query("q") query: String): List<XkcdComicDTO>
}