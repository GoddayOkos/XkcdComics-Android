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

        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.adapter = null
        _binding = null
    }
}