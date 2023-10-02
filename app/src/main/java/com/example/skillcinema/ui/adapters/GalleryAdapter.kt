package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ModelImageBinding
import com.example.skillcinema.entity.Image

class GalleryAdapter(
    private val onItemClick: (Image) -> Unit
) : ListAdapter<Image, GalleryViewHolder>(GalleryDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            ModelImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            Glide
                .with(image)
                .load(item.previewUrl)
                .centerCrop()
                .into(image)

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

class GalleryViewHolder(val binding: ModelImageBinding) : RecyclerView.ViewHolder(binding.root)

class GalleryDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem.imageUrl == newItem.imageUrl

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem.imageUrl == newItem.imageUrl
}
