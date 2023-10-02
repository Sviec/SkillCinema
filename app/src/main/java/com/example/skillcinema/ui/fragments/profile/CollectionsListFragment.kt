package com.example.skillcinema.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.data.db.Collection
import com.example.skillcinema.data.repository.DatabaseRepository
import com.example.skillcinema.databinding.FragmentCollectionsListBinding
import com.example.skillcinema.ui.adapters.CollectionAdapter
import com.example.skillcinema.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class CollectionsListFragment : Fragment() {

    private var _binding: FragmentCollectionsListBinding? = null
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
    private val collectionAdapter: CollectionAdapter =
        CollectionAdapter({ onCollectionClick(it) }, { onDeleteCollectionClick(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.collectionsList.adapter = collectionAdapter

        profileViewModel.collections.onEach {
            collectionAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun onCollectionClick(collection: Collection) {
        lifecycleScope.launch {
            val act =
                ProfileFragmentDirections.actionProfileFragmentToCollectionFragment(collection.name, false)
            findNavController().navigate(act)
        }
    }

    private fun onDeleteCollectionClick(collection: Collection) {
        lifecycleScope.launch {
            profileViewModel.deleteCollection(collection)
        }
    }

}