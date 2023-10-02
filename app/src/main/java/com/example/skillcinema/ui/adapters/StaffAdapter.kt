package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ModelStaffBinding
import com.example.skillcinema.entity.Staff

class StaffAdapter(
    private val onItemClick: (Staff) -> Unit
) : ListAdapter<Staff, StaffViewHolder>(StaffDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        return StaffViewHolder(
            ModelStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            Glide
                .with(actorImg)
                .load(item.posterUrl)
                .centerCrop()
                .into(actorImg)
            actorName.text = item.name
            actorFilmName.text = item.description ?: ""
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        private const val maxListSize = 20
    }
}

class StaffViewHolder(val binding: ModelStaffBinding) : RecyclerView.ViewHolder(binding.root)

class StaffDiffUtil : DiffUtil.ItemCallback<Staff>() {
    override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean =
        oldItem.staffId == newItem.staffId

    override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean =
        oldItem.name == newItem.name
}