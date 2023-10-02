package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ModelSearchItemBinding
import com.example.skillcinema.entity.Film

class SearchAdapter(
    private val onItemClick: (Film) -> Unit
) : PagingDataAdapter<Film, SearchViewHolder>(SearchDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ModelSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            Glide
                .with(itemPosterImg.context)
                .load(item?.posterUrlPreview)
                .centerCrop()
                .into(itemPosterImg)
            this.mainTextView.text = item?.name
            this.additionalTextView.text = item?.genres?.joinToString { it.name }
            root.setOnClickListener {
                onItemClick(item!!)
            }
        }
    }
}

class SearchViewHolder(val binding: ModelSearchItemBinding) : RecyclerView.ViewHolder(binding.root)

class SearchDiffUtil : DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
        oldItem.name == newItem.name
}