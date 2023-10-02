package com.example.skillcinema.ui.fragments.profile

import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.skillcinema.data.db.Collection
import com.example.skillcinema.App
import com.example.skillcinema.databinding.FragmentProfileBinding
import com.example.skillcinema.data.db.Film
import com.example.skillcinema.data.repository.DatabaseRepository
import com.example.skillcinema.databinding.DialogAddCollectionBinding
import com.example.skillcinema.databinding.DialogAddToCollectionBinding
import com.example.skillcinema.ui.adapters.CollectionAdapter
import com.example.skillcinema.ui.adapters.DialogCollectionAdapter
import com.example.skillcinema.ui.adapters.FilmDatabaseAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import com.example.skillcinema.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels {
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
    private val cinemaViewModel: CinemaViewModel by activityViewModels()
    private val filmAdapter: FilmDatabaseAdapter = FilmDatabaseAdapter { onFilmClick(it) }
    private val filmViewedAdapter: FilmDatabaseAdapter = FilmDatabaseAdapter { onFilmClick(it) }
    private val collectionAdapter: CollectionAdapter =
        CollectionAdapter({ onCollectionClick(it) }, { onDeleteCollectionClick(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.isViewedList.adapter = filmViewedAdapter
        binding.wasInterestList.adapter = filmAdapter
        binding.collectionsList.adapter = collectionAdapter

        setListeners()
        setCollections()
        setViewedFilms()
    }

    private fun onFilmClick(film: Film) {
        cinemaViewModel.setCurrentFilm(film.filmId)
        val act = ProfileFragmentDirections.actionProfileFragmentToFilmpageFragment()
        findNavController().navigate(act)
    }

    private fun onCollectionClick(collection: Collection) {
        val act =
            ProfileFragmentDirections.actionProfileFragmentToCollectionFragment(collection.name, false)
        findNavController().navigate(act)
    }

    private fun onDeleteCollectionClick(collection: Collection) {
        lifecycleScope.launch {
            profileViewModel.deleteCollection(collection)
        }
    }

    private fun setListeners() {
        binding.allCollectionsLayout.setOnClickListener {
            val act = ProfileFragmentDirections.actionProfileFragmentToCollectionsListFragment()
            findNavController().navigate(act)
        }

        binding.allInterestLayout.setOnClickListener {

        }

        binding.allViewedLayout.setOnClickListener {
            val act = ProfileFragmentDirections.actionProfileFragmentToCollectionFragment("Просмотрено", true)
            findNavController().navigate(act)

        }

        binding.addCollectionBtn.setOnClickListener {
            openAddCollectionDialog()
        }
    }

    private fun setCollections() {
        profileViewModel.collections.onEach {
            binding.collectionCount.text = it.size.toString()
            collectionAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setViewedFilms() {
        profileViewModel.viewed.onEach {
            binding.isViewedCount.text = it.size.toString()
            filmViewedAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun openAddCollectionDialog() {

        val dialogBinding = DialogAddCollectionBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialogBinding.closeDialogButton.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.addCollectionBtn.setOnClickListener {
            profileViewModel.addCollection(dialogBinding.createNameEditText.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}