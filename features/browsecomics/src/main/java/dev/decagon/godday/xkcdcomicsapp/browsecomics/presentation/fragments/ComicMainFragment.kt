package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.decagon.godday.xkcdcomicsapp.browsecomics.databinding.FragmentComicMainBinding
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.adapter.ComicPagerAdapter

@AndroidEntryPoint
class ComicMainFragment : Fragment() {

    private var _binding: FragmentComicMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ComicPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentComicMainBinding.inflate(inflater, container, false)
        adapter = ComicPagerAdapter(childFragmentManager, lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // extract the id the was passed through deeplink from the searchFragment
        val id = requireArguments().getInt("id")

        binding.viewPager.adapter = adapter

        // Scroll to the position of the comic whose comicId matches with the id
        if (id > 0) {
            binding.viewPager.setCurrentItem(id - 1, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.adapter = null
        _binding = null
    }
}