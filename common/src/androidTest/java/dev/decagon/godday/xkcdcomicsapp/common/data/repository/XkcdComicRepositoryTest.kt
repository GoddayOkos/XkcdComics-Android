package dev.decagon.godday.xkcdcomicsapp.common.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import dev.decagon.godday.xkcdcomicsapp.common.data.api.SearchComicApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.XkcdComicsApi
import dev.decagon.godday.xkcdcomicsapp.common.data.api.model.mapper.ApiComicMapper
import dev.decagon.godday.xkcdcomicsapp.common.data.api.utils.FakeServer
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.Cache
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.FavoriteComicsDB
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.RoomCache
import dev.decagon.godday.xkcdcomicsapp.common.data.di.CacheModule
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.domain.repository.ComicRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(CacheModule::class)  // Uninstall the CacheModule so we don't use the real database for testing
class XkcdComicRepositoryTest {

    private val fakeServer = FakeServer()
    private lateinit var repository: ComicRepository
    private lateinit var xkcdComicApi: XkcdComicsApi
    private lateinit var searchComicsApi: SearchComicApi
    private lateinit var cache: Cache

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: FavoriteComicsDB

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Inject
    lateinit var apiComicMapper: ApiComicMapper

    /**
     * A module that provides Room inMemoryDatabase for testing
     */
    @Module
    @InstallIn(SingletonComponent::class)
    object TestCacheModule {

        @Provides
        fun provideRoomDatabase(): FavoriteComicsDB {
            return Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                FavoriteComicsDB::class.java
            )
                .allowMainThreadQueries()  // Allow tests to run db queries on main thread
                .build()
        }
    }

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

        cache = RoomCache(database.favoriteComicsDao())

        repository = XkcdComicRepository(
            xkcdComicApi,
            searchComicsApi,
            apiComicMapper,
            cache
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

    @Test
    fun saveAndRetrieveFavouriteComics() = runBlocking {
        // Given
        repository.saveFavoriteComic(xkcdComics2)
        repository.saveFavoriteComic(xkcdComics1)

        // When
        val favoriteComics = repository.getFavouriteComics()

        // Then
        assertEquals(1, favoriteComics.first().first().id)
    }

    private val xkcdComics1 = XkcdComics(
        id = 1,
        day = "1",
        month = "1",
        title = "Barrel - Part 1",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/barrel_cropped_(1).jpg",
        shortDescription = "Don't we all.",
        _description = "[[Two trees are growing on opposite sides of a sphere.]]" +
                "\\n{{Alt-title: 'Petit' being a reference to Le Petit Prince, " +
                "which I only thought about halfway through the sketch}}"
    )

    private val xkcdComics2 = XkcdComics(
        id = 2,
        day = "1",
        month = "1",
        title = "Petit Trees (sketch)",
        year = "2006",
        imageUrl = "https://imgs.xkcd.com/comics/tree_cropped_(1).jpg",
        shortDescription = "Petit' being a reference to Le Petit Prince, which " +
                "I only thought about halfway through the sketch",
        _description = "[[A boy sits in a barrel which is floating in an ocean.]]\\nBoy: " +
                "I wonder where I'll float next?\\n[[The barrel drifts into the distance. " +
                "Nothing else can be seen.]]\\n{{Alt: Don't we all.}}"
    )
}