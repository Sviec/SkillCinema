package com.example.skillcinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.databinding.FragmentSeasonsBinding
import com.example.skillcinema.ui.adapters.SeasonAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class SeasonsFragment : Fragment() {

    private var _binding: FragmentSeasonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()
    private lateinit var adapter: SeasonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seasonsTitle.text = viewModel.currentFilm.value.name

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.currentSeasons.onEach {
            val currentSeason = it[0]
            adapter = SeasonAdapter(currentSeason.episodes)
            binding.episodesRV.adapter = adapter
            binding.seasonInfo.text =
                "${currentSeason.number} сезон. ${currentSeason.episodes.size} серий"
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}