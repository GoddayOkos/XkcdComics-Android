package dev.decagon.godday.xkcdcomicsapp.common.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.decagon.godday.xkcdcomicsapp.common.data.api.SearchComicApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.XkcdComicsApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.model.mapper.ApiComicMapper
import dev.decagon.godday.xkcdcomicsapp.common.data.api.utils.FakeServer
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import javax.inject.Inject

@HiltAndroidTest
class XkcdComicRepositoryTest {

    private val fakeServer = FakeServer()
    private lateinit var repository: ComicRepository
    private lateinit var xkcdComicApi: XkcdComicsApi
    private lateinit var searchComicsApi: SearchComicApi

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Inject
    lateinit var apiComicMapper: ApiComicMapper


    @Before
    fun setup() {
        fakeServer.start()
        hiltRule.inject()

        xkcdComicApi = retrofitBuilder
            .baseUrl(fakeServer.baseEndpoint)
            .build()
            .create(XkcdComicsApi::class.java)

        searchComicsApi = retrofitBuilder
            .baseUrl(fakeServer.baseEndpoint)
            .build()
            .create(SearchComicApi::class.java)

        repository = XkcdComicRepository(
            xkcdComicApi,
            searchComicsApi,
            apiComicMapper,
        )
    }

    @After
    fun teardown() {
        fakeServer.shutdown()
    }

    @Test
    fun getXkcdComicById_success() {
        // Given
        val expectedComicId = 1L

        fakeServer.setHappyPathDispatcher()

        // When
        val xkcdComic = runBlocking {
            repository.getXkcdComicById(1L)
        }

        // Then
        assertEquals(expectedComicId, xkcdComic?.id)
    }

    @Test
    fun searchXkcdComics_returnsComicListMatchingQuery() {
        // Given
        val searchQuery = "girl"

        fakeServer.setHappyPathDispatcher()

        // When
        val comicList = runBlocking { repository.searchXkcdComic(searchQuery) }

        // Then
        assert(comicList.first().title.contains(searchQuery, ignoreCase = true))
    }
}