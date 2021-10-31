package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.xkcdcomicsapp.browsecomics.databinding.FragmentSingleComicBinding
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicEvent
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.BrowseComicViewState
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.viewmodel.BrowseXkcdComicViewModel
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.presentation.Event
import dev.decagon.godday.xkcdcomicsapp.common.utils.setImage

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

    private val viewModel: BrowseXkcdComicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        viewModel.state.observe(viewLifecycleOwner) { updateScreenState(it) }
    }

    private fun updateScreenState(state: BrowseComicViewState) {
        binding.pbLoading.isVisible = state.loading
        state.xkcdComic?.let {
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
            btnReload.visibility = View.GONE
        }
    }

    private fun handleFailure(failure: Event<Throwable>?) {
        failure?.getContentIfNotHandled()?.message?.let { str ->
            Snackbar.make(binding.root, str, Snackbar.LENGTH_LONG).show()

            binding.btnReload.visibility = View.VISIBLE
        }
    }

    private fun handleNoMoreComics(noMoreComics: Boolean) {
        // TODO
    }

    private fun setupUI() {
        binding.btnReload.setOnClickListener {
            viewModel.onEvent(BrowseComicEvent.RequestNextXkcdComic(param1!!.toLong()))
        }
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