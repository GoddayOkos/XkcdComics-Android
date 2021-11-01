package dev.decagon.godday.xkcdcomicsapp.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.presentation.Event
import dev.decagon.godday.xkcdcomicsapp.common.presentation.XkcdComicAdapter
import dev.decagon.godday.xkcdcomicsapp.search.R
import dev.decagon.godday.xkcdcomicsapp.search.databinding.FragmentSearchBinding
import dev.decagon.godday.xkcdcomicsapp.search.utils.getQueryTextChangeStateFlow
import dev.decagon.godday.xkcdcomicsapp.search.utils.hideKeyboard

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun observeViewStateUpdate(comicsAdapter: XkcdComicAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreenState(it, comicsAdapter)
        }
    }

    private fun updateScreenState(newState: SearchViewState, comicsAdapter: XkcdComicAdapter) {
        when (newState) {
            is SearchViewState.SearchResults -> updateSearchResultState(newState.results, comicsAdapter)
            is SearchViewState.NoResults -> updateNoResultState()
            is SearchViewState.NoSearchQuery -> updateNoSearchQueryState()
            is SearchViewState.InvalidQuery -> updateInvalidQueryState(newState.message)
            is SearchViewState.Searching -> updateSearchingState()
            is SearchViewState.Failure -> updateFailureState(newState.error)
        }
    }

    private fun updateSearchResultState(results: List<XkcdComics>, adapter: XkcdComicAdapter) {
        with(binding) {
            noSearchResultsImageView.isVisible = false
            noSearchResultsText.isVisible = false
            searchRecyclerView.isVisible = true
            initialSearchImageView.isVisible = false
            initialSearchText.isVisible = false
            searchRemotelyProgressBar.isVisible = false
        }
        adapter.submitList(results)
        requireActivity().hideKeyboard()
    }

    private fun updateNoResultState() {
        with(binding) {
            noSearchResultsImageView.isVisible = true
            noSearchResultsText.isVisible = true
            searchRecyclerView.isVisible = false
            initialSearchImageView.isVisible = false
            initialSearchText.isVisible = false
            searchRemotelyProgressBar.isVisible = false
        }
    }

    private fun updateNoSearchQueryState() {
        with(binding) {
            noSearchResultsImageView.isVisible = false
            noSearchResultsText.isVisible = false
            searchRecyclerView.isVisible = false
            initialSearchImageView.isVisible = true
            initialSearchText.text = getString(R.string.initial_text)
            initialSearchText.isVisible = true
            searchRemotelyProgressBar.isVisible = false
        }
    }

    private fun updateInvalidQueryState(message: String) {
        with(binding) {
            noSearchResultsImageView.isVisible = false
            noSearchResultsText.isVisible = false
            searchRecyclerView.isVisible = false
            initialSearchImageView.isVisible = false
            initialSearchText.text = message
            initialSearchText.isVisible = true
            searchRemotelyProgressBar.isVisible = false
        }
    }

    private fun updateSearchingState() {
        with(binding) {
            noSearchResultsImageView.isVisible = false
            noSearchResultsText.isVisible = false
            searchRecyclerView.isVisible = false
            initialSearchImageView.isVisible = false
            initialSearchText.isVisible = false
            searchRemotelyProgressBar.isVisible = true
        }
    }

    private fun updateFailureState(error: Event<Throwable>) {
        requireActivity().hideKeyboard()
        binding.searchRemotelyProgressBar.isVisible = false
        error.getContentIfNotHandled()?.message?.let {
            Snackbar.make(binding.root,
                it, Snackbar.LENGTH_LONG).show()
        }
    }

    // Navigate to the comics screen with the comicId using deeplink
    private fun createAdapter(): XkcdComicAdapter = XkcdComicAdapter().apply {
        setOnComicClickedListener {
            val deepLink = NavDeepLinkRequest.Builder
                .fromUri("xkcdcomicsapp://comicmain/${it.id}".toUri())
                .build()

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_search, true)
                .setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .build()

            findNavController().navigate(deepLink, navOptions)
        }
    }

    private fun setupRecyclerView(comicsAdapter: XkcdComicAdapter) {
        binding.searchRecyclerView.apply {
            adapter = comicsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupUI() {
        viewModel.onEvent(SearchEvent
            .QueryInput(binding.searchWidget.search.getQueryTextChangeStateFlow()))

        val adapter = createAdapter()
        setupRecyclerView(adapter)
        observeViewStateUpdate(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}