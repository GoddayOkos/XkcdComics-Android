package dev.decagon.godday.xkcdcomicsapp.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.xkcdcomicsapp.common.data.cache.model.FavoriteComic
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.presentation.XkcdComicAdapter
import dev.decagon.godday.xkcdcomicsapp.favorites.R
import dev.decagon.godday.xkcdcomicsapp.favorites.databinding.FragmentFavoritesBinding

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun createAdapter(): XkcdComicAdapter = XkcdComicAdapter().apply {
        setOnComicClickedListener {
            val action = FavoritesFragmentDirections
                .actionFavouritesFragmentToFavoriteDetailFragment(FavoriteComic.fromDomain(it))

            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .build()

            findNavController().navigate(action, navOptions)
        }
    }

    private fun setupRecyclerView(comicsAdapter: XkcdComicAdapter) {
        binding.favRecyclerView.apply {
            adapter = comicsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        observeViewStateUpdate(adapter)
    }

    private fun observeViewStateUpdate(adapter: XkcdComicAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreenState(it, adapter)
        }
    }

    private fun updateScreenState(newState: FavoriteComicsViewState, adapter: XkcdComicAdapter) {
        when (newState) {
            is FavoriteComicsViewState.Loading -> updateLoadingState()
            is FavoriteComicsViewState.FavoriteComics ->
                updateFavoriteComicsState(newState.results, adapter)
            is FavoriteComicsViewState.NoFavoriteComics -> updateNoFavoriteComicsState()
        }
    }

    private fun updateLoadingState() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            noFavText.visibility = View.GONE
            favRecyclerView.visibility = View.GONE
        }
    }

    private fun updateFavoriteComicsState(comics: List<XkcdComics>, adapter: XkcdComicAdapter) {
        adapter.submitList(comics)
        with(binding) {
            progressBar.visibility = View.GONE
            noFavText.visibility = View.GONE
            favRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun updateNoFavoriteComicsState() {
        with(binding) {
            progressBar.visibility = View.GONE
            noFavText.visibility = View.VISIBLE
            favRecyclerView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}