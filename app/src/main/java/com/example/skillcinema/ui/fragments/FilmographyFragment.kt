package com.example.skillcinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.databinding.FragmentFilmographyBinding
import com.example.skillcinema.entity.Film
import com.example.skillcinema.ui.adapters.FilmAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()
    private val adapter = FilmAdapter { onItemClick(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filmsRV.adapter = adapter

        viewModel.currentStaff.onEach {
            binding.staffName.text = it.name
            adapter.submitList(it.films)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onItemClick(film: Film) {
        viewModel.setCurrentFilm(film.filmId)
        val act = FilmographyFragmentDirections.actionFilmographyFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }

}