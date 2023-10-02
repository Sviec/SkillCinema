package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.databinding.ModelEpisodeBinding
import com.example.skillcinema.entity.Episode

class SeasonAdapter(
    private val episodes: List<Episode>,
) : RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        return SeasonViewHolder(
            ModelEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        val item = episodes[position]
        with(holder.binding) {
            episodeName.text = "${item.episodeNumber} серия. ${item.name}"
            episodeDate.text = item.releaseDate
        }
    }

    override fun getItemCount(): Int = episodes.size

    inner class SeasonViewHolder(val binding: ModelEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root)
}