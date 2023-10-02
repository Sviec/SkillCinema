package com.example.skillcinema.ui.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.data.entity.CountryDao
import com.example.skillcinema.databinding.FragmentSearchFiltersBinding
import com.example.skillcinema.entity.CountryGenre
import com.example.skillcinema.ui.adapters.SearchFiltersAdapter
import com.example.skillcinema.ui.viewmodels.SearchViewModel


class SearchFiltersFragment : Fragment() {

    private var _binding: FragmentSearchFiltersBinding? = null
    private val binding get() = _binding!!

    private var adapter = SearchFiltersAdapter { onItemClick(it) }
    private val filterValuesList = mutableListOf<CountryGenre>()

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFiltersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: SearchFiltersFragmentArgs by navArgs()

        setAdapter()
        getFilterTypeList(args.category)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setAdapter() {
        val divider = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val dividerResource =
            ResourcesCompat.getDrawable(resources, R.drawable.divider_recyclerview, null)
        divider.setDrawable(dividerResource!!)

        binding.searchFiltersList.adapter = adapter
        binding.searchFiltersList.addItemDecoration(divider)
    }

    private fun getFilterTypeList(filterType: String) {
        val list = when (filterType) {
            KEY_COUNTRY -> {
                binding.settingsTitle.text = "Страны"
                binding.inputEditText.hint = "Выберете страну"
                viewModel.countries.value
            }
            else -> {
                binding.settingsTitle.text = "Жанры"
                binding.inputEditText.hint = "Выберете жанр"
                viewModel.genres.value
            }
        }
        filterValuesList.addAll(list)
        adapter.submitList(list)
    }

    private fun onItemClick(filterType: CountryGenre) {
        when (filterType) {
            is CountryDao -> {
                viewModel.updateFilters(
                    filterFilm = viewModel.getFilters().copy(countries = filterType.name)
                )
                findNavController().navigateUp()
            }
            else -> {
                viewModel.updateFilters(
                    filterFilm = viewModel.getFilters().copy(genres = filterType.name)
                )
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val KEY_COUNTRY = "country"
        const val KEY_GENRE = "genre"
    }
}