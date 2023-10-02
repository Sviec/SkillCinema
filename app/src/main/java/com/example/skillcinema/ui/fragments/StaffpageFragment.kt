package com.example.skillcinema.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.FragmentStaffpageBinding
import com.example.skillcinema.entity.Category
import com.example.skillcinema.entity.Film
import com.example.skillcinema.ui.adapters.FilmAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel

class StaffpageFragment : Fragment() {

    private var _binding: FragmentStaffpageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()

    private lateinit var adapter: FilmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffpageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = Category(
            category = "Лучшее",
            filmList = viewModel.currentStaff.value.films
        )
        adapter = FilmAdapter(10, category, { onShowAllClick(category) }, { onItemClick(it) })

        binding.bestFilmsRV.adapter = adapter

        binding.watchAllBestFilmsBtn.setOnClickListener {
            onShowAllClick(category)
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.watchAllFilmographyBtn.setOnClickListener {
            val act = StaffpageFragmentDirections.actionStaffpageFragmentToFilmographyFragment()
            findNavController().navigate(act)
        }

        setStaffDetail()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setStaffDetail() {
        lifecycleScope.launchWhenStarted {
            viewModel.currentStaff.collect { staff ->
                binding.staffName.text = staff.name
                binding.staffDescription.text = staff.profession
                binding.filmsCount.text = "${staff.films.size} фильма"
                Glide
                    .with(binding.staffImage)
                    .load(staff.posterUrl)
                    .centerCrop()
                    .into(binding.staffImage)
                adapter.submitList(staff.films.sortedBy { it.rating })
            }
        }
    }

    private fun onShowAllClick(category: Category) {
        viewModel.setCurrentCategory(category)
        val act = StaffpageFragmentDirections.actionStaffpageFragmentToListpageFragment()
        findNavController().navigate(act)
    }

    private fun onItemClick(film: Film) {
        viewModel.setCurrentFilm(film.filmId)
        val act = StaffpageFragmentDirections.actionStaffpageFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }
}