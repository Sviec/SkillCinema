package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.db.Collection
import com.example.skillcinema.databinding.ModelCategoryBinding
import com.example.skillcinema.entity.Category
import com.example.skillcinema.entity.Film

class CategoryAdapter(
    private val onAllItemsClick: (Category) -> Unit,
    private val onItemClick: (Film) -> Unit
) : ListAdapter<Category, CategoryViewHolder>(CategoryDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ModelCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        val adapter = FilmAdapter(20, item, { onAllItemsClick(it) }) { onItemClick(it) }
        adapter.submitList(item.filmList)
        with(holder.binding) {
            filmsList.adapter = adapter
            categoryName.text = item.category
            watchAllBtn.setOnClickListener {
                onAllItemsClick(item)
            }
        }
    }
}

class CategoryViewHolder(val binding: ModelCategoryBinding) :
    RecyclerView.ViewHolder(binding.root)

class CategoryDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem.category == newItem.category

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
        oldItem == newItem
}

