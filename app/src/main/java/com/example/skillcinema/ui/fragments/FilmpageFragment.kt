package com.example.skillcinema.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import com.example.skillcinema.App
import com.example.skillcinema.data.repository.DatabaseRepository
import com.example.skillcinema.databinding.DialogAddToCollectionBinding
import com.example.skillcinema.databinding.FragmentFilmpageBinding
import com.example.skillcinema.entity.*
import com.example.skillcinema.ui.adapters.DialogCollectionAdapter
import com.example.skillcinema.ui.adapters.FilmAdapter
import com.example.skillcinema.ui.adapters.GalleryAdapter
import com.example.skillcinema.ui.adapters.StaffAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import com.example.skillcinema.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class FilmpageFragment : Fragment() {
    private var _binding: FragmentFilmpageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()
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
    private val filmAdapter: FilmAdapter = FilmAdapter(20) { onFilmClick(it) }
    private val actorsAdapter: StaffAdapter = StaffAdapter { onStaffClick(it) }
    private val workersAdapter: StaffAdapter = StaffAdapter { onStaffClick(it) }
    private val galleryAdapter: GalleryAdapter = GalleryAdapter { onImageClick(it) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmpageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setFilmDetail()
        setButtonsState()
        setActors()
        setWorkers()
        setGallery()
        setSimilars()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setListeners() {
        val filmById = viewModel.currentFilm.value

        val film = object : Film {
            override val filmId: Int = filmById.kinopoiskId
            override val posterUrlPreview: String = filmById.posterUrl
            override val rating: Float? = filmById.ratingKinopoisk
            override val name: String = filmById.name
            override val genres: List<CountryGenre> = filmById.genres
        }
        binding.apply {
            actorsList.adapter = actorsAdapter
            workersList.adapter = workersAdapter
            similarsList.adapter = filmAdapter
            gallery.adapter = galleryAdapter
            back.setOnClickListener {
                findNavController().navigateUp()
            }
            watchAllBtn.setOnClickListener {
                val act = FilmpageFragmentDirections.actionFilmpageFragmentToSeasonsFragment()
                findNavController().navigate(act)
            }
            watchAllActorsBtn.setOnClickListener {
                val act = FilmpageFragmentDirections
                    .actionFilmpageFragmentToStaffListpageFragment("ACTORS")
                findNavController().navigate(act)
            }
            watchAllWorkersBtn.setOnClickListener {
                val act = FilmpageFragmentDirections
                    .actionFilmpageFragmentToStaffListpageFragment("WORKERS")
                findNavController().navigate(act)
            }
            watchAllGalleryBtn.setOnClickListener {
                val act = FilmpageFragmentDirections.actionFilmpageFragmentToGalleryFragment()
                findNavController().navigate(act)
            }
            watchAllSimilarsBtn.setOnClickListener {
                val act =
                    FilmpageFragmentDirections.actionFilmpageFragmentToSimilarListpageFragment()
                findNavController().navigate(act)
            }

            favouritesButton.setOnClickListener {
                if (favouritesButton.isChecked) {
                    lifecycleScope.launch {
                        profileViewModel.addFilmToFavourites(film)
                    }
                } else {
                    lifecycleScope.launch {
                        profileViewModel.deleteFilmFromCollection(film, "Избранное")
                    }
                }

            }
            likeButton.setOnClickListener {
                if (likeButton.isChecked) {
                    lifecycleScope.launch {
                        profileViewModel.addFilmToLikes(film)
                    }
                } else {
                    lifecycleScope.launch {
                        profileViewModel.deleteFilmFromCollection(film, "Любимое")
                    }
                }

            }
            hideButton.setOnClickListener {
                if (hideButton.isChecked) {
                    lifecycleScope.launch {
                        profileViewModel.addFilmToIsViewed(film)
                    }
                } else {
                    lifecycleScope.launch {
                        profileViewModel.deleteViewedFilm(film)
                    }
                }

            }
            shareButton.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "https://www.imdb.com/title/${film.filmId}/;")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
            optionalMenuButton.setOnClickListener {
                openDialog(film)
            }
        }
    }

    private fun openDialog(film: Film) {
        val adapter = DialogCollectionAdapter {
            profileViewModel.collectionList.add(it)
        }
        val dialogBinding = DialogAddToCollectionBinding.inflate(layoutInflater)
        dialogBinding.collectionList.adapter = adapter
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        Glide
            .with(dialogBinding.itemPosterImg)
            .load(film.posterUrlPreview)
            .centerCrop()
            .into(dialogBinding.itemPosterImg)
        dialogBinding.mainTextView.text = film.name
        dialogBinding.additionalTextView.text = film.genres.joinToString { it.name }
        dialogBinding.closeDialogButton.setOnClickListener {
            dialog.dismiss()
        }
        profileViewModel.collections.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        dialogBinding.saveButton.setOnClickListener {
            for (collection in profileViewModel.collectionList) {
                profileViewModel.addFilmToCollection(film, collection)
            }
            dialog.dismiss()
        }
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun setFilmDetail() {
        lifecycleScope.launchWhenStarted {
            viewModel.currentFilm.collect { film ->
                profileViewModel.checkCurrentFilm(film)


                binding.filmInfo.text = "${film.ratingKinopoisk} ${film.name}\n" +
                        "${film.genres.joinToString { it.name }}\n" +
                        "${film.countries.joinToString { it.name }} ${film.filmLength ?: ""}"
                Glide
                    .with(binding.posterImg)
                    .load(film.posterUrl)
                    .centerCrop()
                    .into(binding.posterImg)
                binding.shortDescriptionText.text = film.description
                binding.fullDescriptionText.text = film.shortDescription
                if (film.serial) {
                    binding.series.visibility = View.VISIBLE
                    binding.watchAllBtn.setOnClickListener {
                        val act =
                            FilmpageFragmentDirections.actionFilmpageFragmentToSeasonsFragment()
                        findNavController().navigate(act)
                    }
                    viewModel.currentSeasons.onEach {
                        var episodes = 0
                        for (season in it) {
                            episodes += season.episodes.size
                        }
                        binding.seriesInfo.text = "Сезонов: ${it.size}, Серий: $episodes"
                    }
                }
            }

        }
    }

    private fun setButtonsState() {
        lifecycleScope.launchWhenStarted {
            profileViewModel.currentFilm.collect { film ->
                if (film.like) binding.likeButton.isChecked = true
                if (film.favourite) binding.favouritesButton.isChecked = true
                if (film.isViewed) binding.hideButton.isChecked = true
            }
        }
    }

    private fun setActors() {
        viewModel.currentFilmActors.onEach {
            binding.actorsCount.text = it.size.toString()
            actorsAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setWorkers() {
        viewModel.currentFilmWorkers.onEach {
            binding.workersCount.text = it.size.toString()
            workersAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setGallery() {
        viewModel.currentFilmGallery.onEach {
            binding.galleryCount.text = it.size.toString()
            galleryAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setSimilars() {
        viewModel.currentFilmSimilar.onEach {
            binding.similarsCount.text = it.size.toString()
            filmAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onFilmClick(film: Film) {
        viewModel.setCurrentFilm(film.filmId)
        val act = FilmpageFragmentDirections.actionFilmpageFragmentSelf()
        findNavController().navigate(act)
    }

    private fun onStaffClick(staff: Staff) {
        viewModel.setCurrentStaff(staff.staffId)
        val act = FilmpageFragmentDirections.actionFilmpageFragmentToStaffpageFragment()
        findNavController().navigate(act)
    }

    private fun onImageClick(image: Image) {
        val act = FilmpageFragmentDirections
            .actionFilmpageFragmentToGalleryImageFullScreenFragment(image.imageUrl)
        findNavController().navigate(act)
    }
}