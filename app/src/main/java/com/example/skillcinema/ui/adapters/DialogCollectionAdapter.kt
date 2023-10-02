package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.db.Collection
import com.example.skillcinema.databinding.ModelCollectionForDialogBinding

class DialogCollectionAdapter(
    private val onItemClick: (Collection) -> Unit
) :
    ListAdapter<Collection, DialogCollectionViewHolder>(DialogCollectionDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogCollectionViewHolder {
        return DialogCollectionViewHolder(
            ModelCollectionForDialogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DialogCollectionViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
//            Glide
//                .with(collectionImage)
//                .load(item.image)
//                .centerCrop()
//                .into(collectionImage)
            collectionName.text = item.name
            collectionItemsCount.text = item.filmsCount.toString()

            root.setOnClickListener {
                if (!collectionIcon.isChecked) {
                    onItemClick(item)
                    collectionIcon.isChecked = true
                } else {
                    collectionIcon.isChecked = false
                }
            }
        }
    }
}

class DialogCollectionViewHolder(val binding: ModelCollectionForDialogBinding) :
    RecyclerView.ViewHolder(binding.root)

class DialogCollectionDiffUtil : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean =
        oldItem == newItem
}
