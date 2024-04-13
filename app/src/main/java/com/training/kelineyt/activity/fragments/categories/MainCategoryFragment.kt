package com.training.kelineyt.activity.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.kelineyt.R
import com.training.kelineyt.activity.adapters.BestDealsAdapter
import com.training.kelineyt.activity.adapters.BestProductsAdapter
import com.training.kelineyt.activity.adapters.SpecialProductsAdapter
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.viewmodel.MainCategoryViewModel
import com.training.kelineyt.databinding.FragmentMainCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {

    private lateinit var binding: FragmentMainCategoryBinding

    private lateinit var specialProductsAdapter: SpecialProductsAdapter

    private lateinit var bestDealsAdapter: BestDealsAdapter

    private lateinit var bestProductsAdapter: BestProductsAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRV()
        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit

                }
            }
        }

        setupBestDealsRV()
        lifecycleScope.launchWhenStarted {
            viewModel.bestDeals.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }

        }

        setupBestProductsRV()
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductsProgressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductsProgressBar.visibility = View.GONE

                    }

                    is Resource.Error -> {
                        binding.bestProductsProgressBar.visibility = View.GONE
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        increaseItemsWhenIScrollToDown()
    }

    private fun increaseItemsWhenIScrollToDown() {
        binding.nsMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchBestProducts()

            }
        })
    }

    private fun setupBestProductsRV() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun setupBestDealsRV() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun setupSpecialProductsRV() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {

        binding.progressBar.visibility = View.GONE
    }


}