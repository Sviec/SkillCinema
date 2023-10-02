package com.example.skillcinema.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.data.onboarding.OnboardingPage
import com.example.skillcinema.databinding.FragmentOnboardingBinding
import com.example.skillcinema.ui.adapters.OnboardingAdapter
import com.google.android.material.tabs.TabLayoutMediator


class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onboardingListPages: List<OnboardingPage> = listOf(
            OnboardingPage(
                imageId = R.drawable.onboarding_image_first,
                text = resources.getString(R.string.onboarding_description_first)
            ),
            OnboardingPage(
                imageId = R.drawable.onboarding_image_second,
                text = resources.getString(R.string.onboarding_description_second)
            ),
            OnboardingPage(
                imageId = R.drawable.onboarding_image_third,
                text = resources.getString(R.string.onboarding_description_third)
            )
        )

        onboardingAdapter = OnboardingAdapter(onboardingListPages)
        binding.onboardingViewPager.adapter = onboardingAdapter
        TabLayoutMediator(
            binding.onboardingTabLayout,
            binding.onboardingViewPager
        ) { _, _ -> }.attach()

        binding.onboardingSkipText.setOnClickListener {
            val act = OnboardingFragmentDirections.actionOnboardingFragmentToHomepageFragment()
            findNavController().navigate(act)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}