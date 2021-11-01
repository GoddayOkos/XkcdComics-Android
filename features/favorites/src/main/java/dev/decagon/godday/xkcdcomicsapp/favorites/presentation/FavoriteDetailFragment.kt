package dev.decagon.godday.xkcdcomicsapp.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dev.decagon.godday.xkcdcomicsapp.common.utils.setImage
import dev.decagon.godday.xkcdcomicsapp.favorites.databinding.FragmentFavoriteDetailBinding

class FavoriteDetailFragment : Fragment() {

    private val args by navArgs<FavoriteDetailFragmentArgs>()

    private var _binding: FragmentFavoriteDetailBinding? = null
    private val binding: FragmentFavoriteDetailBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        val xkcdComic = args.favoriteComic.toDomain()
        binding.tvTitle.text = String.format("${xkcdComic.id}. ${xkcdComic.title}")
        binding.tvCreateDate.text = xkcdComic.getDatePosted()
        binding.ivXkcdPic.setImage(xkcdComic.imageUrl)
        binding.tvDescription.text = xkcdComic.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}