package com.example.skillcinema.ui.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSearchBinding
import com.example.skillcinema.ui.adapters.SearchAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import com.example.skillcinema.ui.viewmodels.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val cinemaViewModel: CinemaViewModel by activityViewModels()

    private val adapter: SearchAdapter = SearchAdapter { onFilmClick(it.filmId) }

    private var isEditFocused = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setSearchString()
        getFilmList()

        binding.searchFilterBtn.setOnClickListener {
            val act = SearchFragmentDirections.actionSearchFragmentToSearchSettingsFragment()
            findNavController().navigate(act)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.isFilterChanged.collect {
                    if (it) adapter.refresh()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onFilmClick(filmId: Int) {
        cinemaViewModel.setCurrentFilm(filmId)
        val act =
            SearchFragmentDirections.actionSearchFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }

    private fun setAdapter() {
        adapter.addLoadStateListener { state ->
            val currentState = state.refresh
            binding.searchFilmList.visibility =
                if (currentState != LoadState.Loading) View.VISIBLE else View.GONE
            binding.loadingProgress.visibility =
                if (currentState == LoadState.Loading) View.VISIBLE else View.GONE
            binding.searchProgressText.visibility =
                if (currentState != LoadState.Loading) View.VISIBLE else View.GONE
            binding.searchProgressImage.visibility =
                if (currentState == LoadState.Loading) View.VISIBLE else View.GONE
            when (currentState) {
                is LoadState.Loading -> {
                    binding.searchFilmList.visibility = View.GONE
                    binding.searchProgressGroup.visibility = View.VISIBLE
                    binding.searchProgressText.visibility = View.GONE
                    binding.searchProgressImage.visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    binding.searchFilmList.visibility = View.VISIBLE
                    binding.loadingProgress.visibility = View.GONE
                    binding.searchProgressText.visibility = View.GONE
                    binding.searchProgressImage.visibility = View.GONE

                }
                else -> {
                    binding.searchFilmList.visibility = View.GONE
                    binding.loadingProgress.visibility = View.GONE
                    binding.searchProgressText.visibility = View.VISIBLE
                    binding.searchProgressImage.visibility = View.VISIBLE
                }
            }
        }
        binding.searchFilmList.adapter = adapter
    }

    private fun setSearchString() {
        binding.searchView.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            binding.searchGroup.background = if (hasFocus) {
                isEditFocused = true
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.search_input_field_select,
                    null
                )
            } else {
                ResourcesCompat.getDrawable(resources, R.drawable.search_input_field, null)
            }
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.updateFilters(
                    filterFilm = searchViewModel.getFilters().copy(keyword = newText.toString())
                )
                return false
            }
        })
    }

    private fun getFilmList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.films.collectLatest(adapter::submitData)
            }
        }
    }
}