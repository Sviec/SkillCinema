package com.example.skillcinema.ui.adapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ModelCollectionBinding
import com.example.skillcinema.data.db.Collection


class CollectionAdapter(
    private val onItemClick: (Collection) -> Unit,
    private val onDeleteCollectionClick: (Collection) -> Unit
) : ListAdapter<Collection, CollectionViewHolder>(CollectionDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(
            ModelCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {

            when (item.name) {
                "Любимое" -> {
                    setImage(holder, R.drawable.like)
                }
                "Избранное" -> {
                    setImage(holder, R.drawable.bookmark)
                }
                else -> {
                    Glide
                        .with(collectionImage)
                        .load(R.drawable.bottom_icon_profile)
                        .fitCenter()
                        .into(collectionImage)
                }
            }
            collectionName.text = item.name
            collectionItemsCountTextView.text = item.filmsCount.toString()
            deleteBtn.setOnClickListener {
                onDeleteCollectionClick(item)
            }
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun setImage(holder: CollectionViewHolder, image: Int) {
        with(holder.binding) {
            val wrappedDrawable: Drawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(
                    holder.binding.root.context,
                    image
                )!!
            )
            val mutableDrawable: Drawable = wrappedDrawable.mutate()
            DrawableCompat.setTint(
                mutableDrawable,
                ContextCompat.getColor(holder.binding.root.context, R.color.light_black)
            )
            Glide
                .with(collectionImage)
                .load(mutableDrawable)
                .fitCenter()
                .into(collectionImage)
        }
    }
}

class CollectionViewHolder(val binding: ModelCollectionBinding) :
    RecyclerView.ViewHolder(binding.root)

class CollectionDiffUtil : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean =
        oldItem == newItem
}
