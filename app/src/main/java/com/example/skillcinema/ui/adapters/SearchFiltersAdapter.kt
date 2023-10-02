package com.example.skillcinema.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ModelSearchCategoryBinding
import com.example.skillcinema.entity.CountryGenre

class SearchFiltersAdapter(
    private val onItemClick: (CountryGenre) -> Unit
) : ListAdapter<CountryGenre, SearchFiltersViewHolder>(SearchFiltersDiffUtil()) {
    private var singleItemSelectionPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchFiltersViewHolder(
        ModelSearchCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SearchFiltersViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            name.text = item.name
            if (singleItemSelectionPosition == position) {
                root.setBackgroundColor(holder.binding.root.resources.getColor(R.color.light_grey))
            } else {
                root.setBackgroundColor(Color.TRANSPARENT)
            }
            root.setOnClickListener {
                setSingleSelection(position)
                onItemClick(item)
            }
        }
    }

    private fun setSingleSelection(adapterPosition: Int) {
        if (adapterPosition == RecyclerView.NO_POSITION) return

        notifyItemChanged(singleItemSelectionPosition)
        singleItemSelectionPosition = adapterPosition
        notifyItemChanged(singleItemSelectionPosition)
    }
}

class SearchFiltersViewHolder(val binding: ModelSearchCategoryBinding) :
    RecyclerView.ViewHolder(binding.root)

class SearchFiltersDiffUtil : DiffUtil.ItemCallback<CountryGenre>() {
    override fun areItemsTheSame(oldItem: CountryGenre, newItem: CountryGenre) =
        oldItem.name == newItem.name

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: CountryGenre, newItem: CountryGenre) =
        oldItem == newItem
}