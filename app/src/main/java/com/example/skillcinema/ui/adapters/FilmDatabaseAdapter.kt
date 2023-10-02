package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.db.Film
import com.example.skillcinema.databinding.ModelFilmBinding

class FilmDatabaseAdapter(
    private val onItemClick: (Film) -> Unit
) : ListAdapter<Film, FilmDatabaseViewHolder>(FilmDatabaseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmDatabaseViewHolder {
        return FilmDatabaseViewHolder(
            ModelFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilmDatabaseViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            Glide
                .with(itemPosterImg)
                .load(item.posterUrlPreview)
                .centerCrop()
                .into(itemPosterImg)
            if (item.rating != null)
                itemRating.text = item.rating.toString()
            else itemRating.visibility = View.GONE
            itemName.text = item.name
            itemGenre.text = item.genres
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

class FilmDatabaseViewHolder(val binding: ModelFilmBinding) : RecyclerView.ViewHolder(binding.root)

class FilmDatabaseDiffUtil : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.name == newItem.name
}