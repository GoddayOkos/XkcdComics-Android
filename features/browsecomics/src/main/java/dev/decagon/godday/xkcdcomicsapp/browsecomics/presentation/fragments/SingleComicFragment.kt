package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.xkcdcomicsapp.browsecomics.R
import dev.decagon.godday.xkcdcomicsapp.browsecomics.databinding.FragmentSingleComicBinding
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicEvent
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicViewEffect
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicViewState
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.viewmodel.BrowseXkcdComicViewModel
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.presentation.Event
import dev.decagon.godday.xkcdcomicsapp.common.utils.setImage
import kotlinx.coroutines.flow.collect

/**
 * A simple [Fragment] subclass.
 * Use the [SingleComicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SingleComicFragment : Fragment() {

    private var param1: Int? = null
    private var _binding: FragmentSingleComicBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentComic: XkcdComics

    private val viewModel: BrowseXkcdComicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.onEvent(BrowseComicEvent.RequestNextXkcdComic(param1!!.toLong()))
        _binding = FragmentSingleComicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewEffects()
        viewModel.state.observe(viewLifecycleOwner) { updateScreenState(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.comics_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search ->  navigateToSearchFragment()
            R.id.share -> shareXkcdComic()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeViewEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewEffects.collect { reactTo(it) }
        }
    }

    private fun reactTo(effect: BrowseComicViewEffect) {
        when (effect) {
            is BrowseComicViewEffect.OnFavoriteComicAdded -> {
                Snackbar.make(binding.root, getString(R.string.comic_added_msg), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateScreenState(state: BrowseComicViewState) {
        binding.pbLoading.isVisible = state.loading
        state.xkcdComic?.let {
            currentComic = it
            displayComic(it)
        }

        handleNoMoreComics(state.noMoreXkcdComics)
        handleFailure(state.failure)
    }

    private fun displayComic(xkcdComic: XkcdComics) {
        with(binding) {
            tvTitle.text = String.format("${xkcdComic.id}. ${xkcdComic.title}")
            tvCreateDate.text = xkcdComic.getDatePosted()
            ivXkcdPic.setImage(xkcdComic.imageUrl)
            tvDescription.text = xkcdComic.description
            binding.fab.visibility = View.VISIBLE
            btnReload.visibility = View.GONE
        }
    }

    private fun handleFailure(failure: Event<Throwable>?) {
        failure?.getContentIfNotHandled()?.message?.let { str ->
            Snackbar.make(binding.root, str, Snackbar.LENGTH_LONG).show()

            binding.btnReload.visibility = View.VISIBLE
            binding.fab.visibility = View.GONE
        }
    }

    private fun handleNoMoreComics(noMoreComics: Boolean) {
        // TODO
    }

    private fun setupUI() {
        with(binding) {
            btnReload.setOnClickListener {
                viewModel.onEvent(BrowseComicEvent.RequestNextXkcdComic(param1!!.toLong()))
            }

            fab.setOnClickListener {
                viewModel.onEvent(BrowseComicEvent.AddComicToFavorite(currentComic))
            }
        }
    }

    private fun shareXkcdComic() {
        if (!this::currentComic.isInitialized) {
            Snackbar.make(binding.root, getString(R.string.no_comics_msg), Snackbar.LENGTH_LONG).show()
            return
        }

        val shareComicIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, getString(R.string.share_comic_msg, currentComic.imageUrl))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_comic_subject))
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareComicIntent, getString(R.string.share_comic_title)))
    }

    private fun navigateToSearchFragment() {
        // Todo: Replace with code to navigate to search screen
        Snackbar.make(binding.root, "Search icon clicked", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clear()
        _binding = null
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment SingleComicFragment.
         */
        @JvmStatic
        fun newInstance(param1: Int) =
            SingleComicFragment().apply {
                arguments = bundleOf(ARG_PARAM1 to param1)
            }
    }
}