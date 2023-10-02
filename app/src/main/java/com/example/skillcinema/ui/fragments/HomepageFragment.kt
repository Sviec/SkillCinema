package com.example.skillcinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.skillcinema.databinding.FragmentHomepageBinding
import com.example.skillcinema.entity.Category
import com.example.skillcinema.entity.Film
import com.example.skillcinema.ui.adapters.CategoryAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import com.example.skillcinema.utils.StateLoading
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()
    private var adapter: CategoryAdapter =
        CategoryAdapter({ onAllItemsClick(it) }, { onItemClick(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkOnboarding()
        stateLoadingListener()

        binding.categoryRv.adapter = adapter

        viewModel.categories.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkOnboarding() {
        PreferenceManager.getDefaultSharedPreferences(requireContext()).apply {
            if (!contains(PREFERENCES_NAME)) {
                edit().putBoolean(PREFERENCES_NAME, true).apply()
                val act = HomepageFragmentDirections.actionHomepageFragmentToOnboardingFragment()
                findNavController().navigate(act)
            }
        }
    }

    private fun stateLoadingListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loadCategoryState.collect { state ->
                when (state) {
                    is StateLoading.Loading -> {
                        binding.progressGroup.isVisible = true
                        binding.loadingProgress.isVisible = true
                        binding.loadingRefreshBtn.isVisible = false
                        binding.categoryRv.isVisible = false
                    }
                    is StateLoading.Success -> {
                        binding.progressGroup.isVisible = false
                        binding.categoryRv.isVisible = true
                    }
                    else -> {
                        binding.progressGroup.isVisible = true
                        binding.loadingProgress.isVisible = false
                        binding.loadingRefreshBtn.isVisible = true
                        binding.categoryRv.isVisible = false
                        binding.loadingRefreshBtn.setOnClickListener { viewModel.getFilmsCategories() }
                    }
                }
            }
        }
    }

    private fun onAllItemsClick(category: Category) {
        viewModel.setCurrentCategory(category)
        val act = HomepageFragmentDirections.actionHomepageFragmentToListpageFragment()
        findNavController().navigate(act)
    }

    private fun onItemClick(film: Film) {
        viewModel.setCurrentFilm(film.filmId)
        val act = HomepageFragmentDirections.actionHomepageFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }

    companion object {
        private const val PREFERENCES_NAME = "pref_name"
    }
}