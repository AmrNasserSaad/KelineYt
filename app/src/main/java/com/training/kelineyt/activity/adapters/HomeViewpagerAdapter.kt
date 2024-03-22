package com.training.kelineyt.activity.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle

class HomeViewpagerAdapter(

    private val fragments : List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle:Lifecycle

):FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}