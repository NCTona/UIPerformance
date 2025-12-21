package com.tona.myapplication.perceiveddemo

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PerceivedPagerAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> SpinnerDemoFragment()
            else -> OptimisticDemoFragment()
        }
}
