package com.training.kelineyt.activity.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.training.kelineyt.databinding.FragmentLanguageBinding

class LanguageFragment:Fragment() {
    private lateinit var binding: FragmentLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeLanguage()

        binding.imageAddressClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun changeLanguage() {


//        binding.linearArabic.setOnClickListener {
//
//        }
//        binding.linearEnglish.setOnClickListener {
//
//        }

    }
}