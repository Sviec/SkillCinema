package com.example.skillcinema.ui.fragments.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.App
import com.example.skillcinema.data.db.Film
import com.example.skillcinema.data.repository.DatabaseRepository
import com.example.skillcinema.databinding.FragmentCollectionBinding
import com.example.skillcinema.ui.adapters.FilmDatabaseAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import com.example.skillcinema.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class CollectionFragment : Fragment() {
    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    private val cinemaViewModel: CinemaViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val filmDao = (requireActivity().application as App).db.filmDao()
                val filmCollectionDao =
                    (requireActivity().application as App).db.filmCollectionDao()
                val collectionDao = (requireActivity().application as App).db.collectionDao()

                return ProfileViewModel(
                    DatabaseRepository(
                        filmDao = filmDao,
                        filmCollectionDao = filmCollectionDao,
                        collectionDao = collectionDao
                    )
                ) as T
            }
        }
    }
    private val adapter: FilmDatabaseAdapter = FilmDatabaseAdapter { onItemClick(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filmsList.adapter = adapter

        val navArgs: CollectionFragmentArgs by navArgs()

        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.setCurrentCollection(navArgs.collectionName)
        }

        binding.collectionTitle.text = navArgs.collectionName
        if (navArgs.isViewed) {
            profileViewModel.viewed.onEach {
                adapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        } else {
            profileViewModel.currentCollection.onEach {
                adapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }


        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onItemClick(film: Film) {
        cinemaViewModel.setCurrentFilm(film.filmId)
        val act = CollectionFragmentDirections.actionCollectionFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}