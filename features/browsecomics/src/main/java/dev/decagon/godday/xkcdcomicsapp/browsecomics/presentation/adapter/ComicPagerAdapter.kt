package dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.ComicContract
import dev.decagon.godday.xkcdcomicsapp.browsecomics.presentation.fragments.SingleComicFragment

class ComicPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private val contract: ComicContract) :
    FragmentStateAdapter(fm, lifecycle) {

    /*
        size is set to Int.MAX_VALUE / 2 since the number of comics from the
        endpoint is dynamic. This will enable infinite scrolling and this size
        will be changed to the index + 1 of the last comic when user has gotten to the end of the
        comics from the endpoint. This will enable us stop the forward scroll.
     */
    var size = Int.MAX_VALUE / 2
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment =
        SingleComicFragment.newInstance(position + 1, contract)
}