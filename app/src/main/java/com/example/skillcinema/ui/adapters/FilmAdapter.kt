package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ModelFilmBinding
import com.example.skillcinema.entity.Category
import com.example.skillcinema.entity.Film

class FilmAdapter(
    private val maxListSize: Int,
    private val category: Category? = null,
    private val onShowAllClick: ((Category) -> Unit)? = null,
    private val onItemClick: (Film) -> Unit
) : ListAdapter<Film, FilmViewHolder>(FilmDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            ModelFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val item = getItem(position)

        if (position < maxListSize - 1) {
            with(holder.binding) {
                filmCard.visibility = View.VISIBLE
                showAll.visibility = View.GONE
                Glide
                    .with(itemPosterImg.context)
                    .load(item.posterUrlPreview)
                    .centerCrop()
                    .into(itemPosterImg)
                if (item.rating != null)
                    itemRating.text = item.rating.toString()
                else itemRating.visibility = View.GONE
                itemName.text = item.name
                itemGenre.text = item.genres.joinToString { it.name }
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        } else if (position == maxListSize - 1){
            with(holder.binding) {
                filmCard.visibility = View.GONE
                showAll.visibility = View.VISIBLE
                root.setOnClickListener {
                    onShowAllClick?.invoke(category!!)
                }
            }
        }
    }

}

class FilmViewHolder(val binding: ModelFilmBinding) : RecyclerView.ViewHolder(binding.root)

class FilmDiffUtil : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.name == newItem.name
}
