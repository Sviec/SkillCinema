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
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.skillcinema.databinding.FragmentStaffListpageBinding
import com.example.skillcinema.entity.Staff
import com.example.skillcinema.ui.adapters.StaffAdapter
import com.example.skillcinema.ui.viewmodels.CinemaViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class StaffListpageFragment : Fragment() {
    private var _binding: FragmentStaffListpageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by activityViewModels()
    private val adapter: StaffAdapter = StaffAdapter { onItemClick(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffListpageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: StaffListpageFragmentArgs by navArgs()

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        setAdapter()
        if (args.staffType == "ACTORS") {
            setActors()
        } else if (args.staffType == "WORKERS") {
            setWorkers()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onItemClick(staff: Staff) {
        viewModel.setCurrentStaff(staff.staffId)
        val act = StaffListpageFragmentDirections.actionStaffListpageFragmentToStaffpageFragment()
        findNavController().navigate(act)
    }

    private fun setAdapter() {
        adapter.addLoadStateListener { state ->
            val currentState = state.refresh
            binding.staffList.visibility =
                if (currentState != LoadState.Loading) View.VISIBLE else View.GONE
            binding.loadingProgress.visibility =
                if (currentState == LoadState.Loading) View.VISIBLE else View.GONE
            binding.searchProgressText.visibility =
                if (currentState != LoadState.Loading) View.VISIBLE else View.GONE
            binding.searchProgressImage.visibility =
                if (currentState == LoadState.Loading) View.VISIBLE else View.GONE
            when (currentState) {
                is LoadState.Loading -> {
                    binding.staffList.visibility = View.GONE
                    binding.searchProgressGroup.visibility = View.VISIBLE
                    binding.searchProgressText.visibility = View.GONE
                    binding.searchProgressImage.visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    binding.staffList.visibility = View.VISIBLE
                    binding.loadingProgress.visibility = View.GONE
                    binding.searchProgressText.visibility = View.GONE
                    binding.searchProgressImage.visibility = View.GONE

                }
                else -> {
                    binding.staffList.visibility = View.GONE
                    binding.loadingProgress.visibility = View.GONE
                    binding.searchProgressText.visibility = View.VISIBLE
                    binding.searchProgressImage.visibility = View.VISIBLE
                }
            }
        }
        binding.staffList.adapter = adapter
    }

    private fun setActors() {
//        binding.staffListpageTitle.text = "В фильме снимались"
//        viewModel.currentFilmActors.onEach {
//            adapter.submitList(it)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentFilmActors.collectLatest(adapter::submitData)
            }
        }
    }

    private fun setWorkers() {
//        binding.staffListpageTitle.text = "Над фильмом работали"
//        viewModel.currentFilmWorkers.onEach {
//            adapter.submitList(it)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentFilmWorkers.collectLatest(adapter::submitData)
            }
        }
    }
}