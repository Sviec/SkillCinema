package com.example.skillcinema.ui.fragments.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSearchSettingsBinding
import com.example.skillcinema.ui.viewmodels.SearchViewModel
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider


class SearchSettingsFragment : Fragment() {
    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.countrySettings.setOnClickListener {
            filterTypeChooseClick(SearchFiltersFragment.KEY_COUNTRY)
        }
        binding.genreSettings.setOnClickListener {
            filterTypeChooseClick(SearchFiltersFragment.KEY_GENRE)
        }

        setRatingSlider()
        setLastFilters()
        setRadioGroups()
    }

    private fun setLastFilters() {
        val filters = viewModel.getFilters()
        when (filters.type) {
            "ALL" -> binding.allRadioBtn.isChecked = true
            "FILM" -> binding.filmsRadioBtn.isChecked = true
            "TV_SERIES" -> binding.seriesRadioBtn.isChecked = true
        }
        binding.countryTextView.text = filters.countries.ifEmpty { "Все страны" }
        binding.genreTextView.text = filters.genres.ifEmpty { "Все жанры" }

        binding.searchSettingsRatingSlider.valueFrom = filters.ratingFrom.toFloat()
        binding.searchSettingsRatingSlider.valueTo = filters.ratingTo.toFloat()
        when (filters.order) {
            "YEAR" -> binding.sortingByDate.isChecked = true
            "RATING" -> binding.sortingByRating.isChecked = true
            "NUM_VOTE" -> binding.sortingByPopular.isChecked = true
        }
    }

    private fun setRadioGroups() {
        binding.radioGroupShowCinemaCategory.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.allRadioBtn -> viewModel.updateFilters(
                    viewModel.getFilters().copy(
                        type = "ALL"
                    )
                )
                R.id.filmsRadioBtn -> viewModel.updateFilters(
                    viewModel.getFilters().copy(
                        type = "FILM"
                    )
                )
                R.id.seriesRadioBtn -> viewModel.updateFilters(
                    viewModel.getFilters().copy(
                        type = "TV_SERIES"
                    )
                )
            }
        }

        binding.radioGroupSortCinema.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.sortingByDate -> viewModel.updateFilters(
                    viewModel.getFilters().copy(
                        order = "YEAR"
                    )
                )
                R.id.sortingByRating -> viewModel.updateFilters(
                    viewModel.getFilters().copy(
                        order = "RATING"
                    )
                )
                R.id.sortingByPopular -> viewModel.updateFilters(
                    viewModel.getFilters().copy(
                        order = "NUM_VOTE"
                    )
                )
            }
        }
    }

    private fun filterTypeChooseClick(filterType: String) {
        val action = SearchSettingsFragmentDirections
            .actionSearchSettingsFragmentToSearchFiltersFragment(filterType)
        findNavController().navigate(action)
    }

    private fun setRatingSlider() {
        binding.searchSettingsRatingSlider.addOnSliderTouchListener(object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val sliderValues = slider.values
                viewModel.updateFilters(
                    viewModel.getFilters().copy(
                        ratingFrom = sliderValues[0].toInt(),
                        ratingTo = sliderValues[1].toInt()
                    )
                )
            }
        })
        binding.searchSettingsRatingSlider.addOnChangeListener { slider, value, fromUser ->
            val sliderValues = slider.values
            if (sliderValues[0] == 0F && sliderValues[1] == 10F) {
                binding.ratingTextView.text = "любой"
            } else {
                binding.ratingTextView.text =
                    "От ${sliderValues[0].toInt()} до ${sliderValues[1].toInt()}"
                binding.ratingStart.text = sliderValues[0].toInt().toString()
                binding.ratingEnd.text = sliderValues[1].toInt().toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}