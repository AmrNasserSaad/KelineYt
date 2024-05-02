package com.training.kelineyt.activity.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.training.kelineyt.R
import com.training.kelineyt.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToCategory()

    }

    private fun navigateToCategory() {
        binding.cardTable.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_tableFragment)
        }
        binding.cardAccessory.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_accessoryFragment)
        }
        binding.cardChair.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_chairFragment)
        }
        binding.cardCupboard.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_cupboardFragment)
        }
        binding.cardFurniture.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_furnitureFragment)
        }
    }
}