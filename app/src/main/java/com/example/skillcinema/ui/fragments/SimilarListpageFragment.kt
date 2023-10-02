package com.example.skillcinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.databinding.FragmentSimilarListpageBinding
import com.example.skillcinema.entity.Film
import com.example.skillcinema.ui.adapters.FilmAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class SimilarListpageFragment : Fragment() {

    private var _binding: FragmentSimilarListpageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()
    private val adapter: FilmAdapter = FilmAdapter { onItemClick(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarListpageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.similarsList.adapter = adapter
//        viewModel.currentFilmSimilar.onEach {
//            adapter.submitList(it)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentFilmSimilar.collectLatest(adapter::submitData)
            }
        }
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
        val act = SimilarListpageFragmentDirections
            .actionSimilarListpageFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }

}