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
import androidx.paging.LoadState
import com.example.skillcinema.databinding.FragmentListpageBinding
import com.example.skillcinema.entity.Film
import com.example.skillcinema.ui.adapters.FilmAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ListpageFragment : Fragment() {

    private var _binding: FragmentListpageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()
    private val adapter: FilmAdapter = FilmAdapter { onItemClick(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListpageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setAdapter()
        getFilmsList()

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
        val act = ListpageFragmentDirections.actionListpageFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }

//    private fun setAdapter() {
//        adapter.addLoadStateListener { state ->
//            val currentState = state.refresh
//            binding.filmsList.visibility =
//                if (currentState != LoadState.Loading) View.VISIBLE else View.GONE
//            binding.loadingProgress.visibility =
//                if (currentState == LoadState.Loading) View.VISIBLE else View.GONE
//            binding.searchProgressText.visibility =
//                if (currentState != LoadState.Loading) View.VISIBLE else View.GONE
//            binding.searchProgressImage.visibility =
//                if (currentState == LoadState.Loading) View.VISIBLE else View.GONE
//            when (currentState) {
//                is LoadState.Loading -> {
//                    binding.filmsList.visibility = View.GONE
//                    binding.searchProgressGroup.visibility = View.VISIBLE
//                    binding.searchProgressText.visibility = View.GONE
//                    binding.searchProgressImage.visibility = View.VISIBLE
//                }
//                is LoadState.NotLoading -> {
//                    binding.filmsList.visibility = View.VISIBLE
//                    binding.loadingProgress.visibility = View.GONE
//                    binding.searchProgressText.visibility = View.GONE
//                    binding.searchProgressImage.visibility = View.GONE
//
//                }
//                else -> {
//                    binding.filmsList.visibility = View.GONE
//                    binding.loadingProgress.visibility = View.GONE
//                    binding.searchProgressText.visibility = View.VISIBLE
//                    binding.searchProgressImage.visibility = View.VISIBLE
//                }
//            }
//        }
//        binding.filmsList.adapter = adapter
//    }

    private fun getFilmsList() {
        viewModel.currentCategory.onEach {
            adapter.submitList(it.filmList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.currentCategory.collectLatest(adapter::submitData)
//            }
//        }
    }
}