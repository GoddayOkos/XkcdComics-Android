package dev.decagon.godday.xkcdcomicsapp.common.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.decagon.godday.xkcdcomicsapp.common.databinding.ComicRecyclerViewItemBinding
import dev.decagon.godday.xkcdcomicsapp.common.domain.model.XkcdComics
import dev.decagon.godday.xkcdcomicsapp.common.utils.setImage

class XkcdComicAdapter : ListAdapter<XkcdComics, XkcdComicAdapter.ComicsViewHolder>(ITEM_COMPARATOR) {

    private var comicClickListener: ComicClickListener? = null

    inner class ComicsViewHolder(
        private val binding: ComicRecyclerViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comics: XkcdComics) {
            binding.comicTitle.text = comics.title
            binding.thumbnail.setImage(comics.imageUrl)
            binding.comicInfo.text = comics.shortDescription
            binding.id.text = comics.id.toString()

            binding.root.setOnClickListener {
                comicClickListener?.onComicClicked(comics)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        val binding = ComicRecyclerViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ComicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        val comics: XkcdComics = getItem(position)
        holder.bind(comics)
    }

    fun setOnComicClickedListener(comicClickListener: ComicClickListener) {
        this.comicClickListener = comicClickListener
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<XkcdComics>() {
    override fun areItemsTheSame(oldItem: XkcdComics, newItem: XkcdComics): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: XkcdComics, newItem: XkcdComics): Boolean =
        oldItem == newItem
}

fun interface ComicClickListener {
    fun onComicClicked(comic: XkcdComics)
}