package com.example.skillcinema.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.data.onboarding.OnboardingPage
import com.example.skillcinema.databinding.OnboardingModelBinding

class OnboardingAdapter(
    private val onboardingList: List<OnboardingPage>
) : RecyclerView.Adapter<OnboardingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            OnboardingModelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = onboardingList[position]
        with(holder.binding) {
            onboardingImage.setImageResource(item.imageId)
            onboardingText.text = item.text
        }
    }

    override fun getItemCount(): Int = onboardingList.size
}

class OnboardingViewHolder(
    val binding: OnboardingModelBinding
) : RecyclerView.ViewHolder(binding.root)