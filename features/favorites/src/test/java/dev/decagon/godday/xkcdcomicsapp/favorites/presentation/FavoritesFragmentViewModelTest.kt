package dev.decagon.godday.xkcdcomicsapp.favorites.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dev.decagon.godday.xkcdcomicsapp.common.TestCoroutineRule
import dev.decagon.godday.xkcdcomicsapp.common.data.FakeRepository
import dev.decagon.godday.xkcdcomicsapp.favorites.domain.usecases.GetFavoriteComicsFromDB
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesFragmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: FavoritesFragmentViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getFavouriteComicsFromDB: GetFavoriteComicsFromDB

    @Before
    fun setup() {
        repository = FakeRepository()

        getFavouriteComicsFromDB = GetFavoriteComicsFromDB(repository)

        viewModel = FavoritesFragmentViewModel(getFavouriteComicsFromDB)
    }


    @Test
    fun `FavoritesFragmentViewModel returns FavouriteComics state when db is not empty`() {
        testCoroutineRule.runBlockingTest {
            // Given
            val expectedState = FavoriteComicsViewState
                .FavoriteComics(repository.remoteComicList)

            viewModel.state.observeForever { }

            // Then
            val viewState = viewModel.state.value!!

            assertThat(viewState).isEqualTo(expectedState)
        }
    }
}