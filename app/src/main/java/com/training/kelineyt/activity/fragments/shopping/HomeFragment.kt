package com.training.kelineyt.activity.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.training.kelineyt.R
import com.training.kelineyt.activity.adapters.HomeViewpagerAdapter
import com.training.kelineyt.activity.fragments.categories.AccessoryFragment
import com.training.kelineyt.activity.fragments.categories.ChairFragment
import com.training.kelineyt.activity.fragments.categories.CupboardFragment
import com.training.kelineyt.activity.fragments.categories.FurnitureFragment
import com.training.kelineyt.activity.fragments.categories.MainCategoryFragment
import com.training.kelineyt.activity.fragments.categories.TableFragment
import com.training.kelineyt.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(

            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            AccessoryFragment(),
            TableFragment(),
            FurnitureFragment()

        )

        val viewpagerAdapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewpagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome) { tab, position ->

            when (position) {

                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }

        }.attach()

    }


}