package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dev.decagon.godday.xkcdcomicsapp.browsecomics.domain.usecases.GetXkcdComic
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicEvent
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicViewState
import dev.decagon.godday.xkcdcomicsapp.common.TestCoroutineRule
import dev.decagon.godday.xkcdcomicsapp.common.data.FakeRepository
import dev.decagon.godday.xkcdcomicsapp.common.utils.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BrowseXkcdComicViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: BrowseXkcdComicViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getXkcdComic: GetXkcdComic

    @Before
    fun setup() {
        val dispatchersProvider = object : DispatchersProvider {
            override fun io() = Dispatchers.Main
        }

        repository = FakeRepository()
        getXkcdComic = GetXkcdComic(repository)

        viewModel = BrowseXkcdComicViewModel(
            getXkcdComic,
            dispatchersProvider
        )
    }

    @Test
    fun `BrowseXkcdComicViewModel getXkcdComicById returns loading ViewState when no event`() =
        testCoroutineRule.runBlockingTest {

            // Given
            viewModel.state.observeForever { }

            val expectedViewState = BrowseComicViewState(
                loading = true,
                xkcdComic = null,
                noMoreXkcdComics = false,
                failure = null
            )

            // When

            // Then
            val viewState = viewModel.state.value!!

            assertThat(viewState).isEqualTo(expectedViewState)
        }

    @Test
    fun `BrowseXkcdComicViewModel getXkcdComicById returns data ViewState`() =
        testCoroutineRule.runBlockingTest {

            // Given
            val comicId = 2L
            val expectedXkcdComic = repository.xkcdComics2

            viewModel.state.observeForever { }

            val expectedViewState = BrowseComicViewState(
                loading = false,
                xkcdComic = expectedXkcdComic,
                noMoreXkcdComics = false,
                failure = null
            )

            // When
            viewModel.onEvent(BrowseComicEvent.RequestNextXkcdComic(comicId))

            // Then
            val viewState = viewModel.state.value!!

            assertThat(viewState).isEqualTo(expectedViewState)
        }
}