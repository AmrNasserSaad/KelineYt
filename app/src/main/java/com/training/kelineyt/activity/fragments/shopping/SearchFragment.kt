package com.training.kelineyt.activity.fragments.shopping

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.training.kelineyt.R
import com.training.kelineyt.activity.adapters.SearchAdapter
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.viewmodel.SearchViewModel
import com.training.kelineyt.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val searchAdapter by lazy { SearchAdapter(emptyList()) }

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.searchProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        Log.d(TAG, "searchLoading: ")
                        binding.searchProgressBar.visibility = View.VISIBLE
                        hideCardsView()
                    }

                    is Resource.Success -> {
                        Log.d(TAG, "Success:${it.data} ")
                        searchAdapter.addNewListItems(it.data!!)
                        binding.searchProgressBar.visibility = View.GONE
                        hideCardsView()
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "searchFailed: ")
                        binding.searchProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

    }

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
        navigateToProductDetails()
        setupSearchRV()

        binding.imgSearch.setOnClickListener {
            Log.d(TAG, "searchScan: ")
            searchSuccess()
        }


    }

    private fun searchSuccess() {
        Log.d(TAG, "searchSuccess: ")
        val productName: String = binding.edSearch.text.toString()
         viewModel.search(productName)

    }

    private fun navigateToProductDetails() {
        searchAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(
                R.id.action_searchFragment_to_productDetailsFragment,
                bundle
            )
        }
    }

    private fun setupSearchRV() {
        Log.d(TAG, "setUpRV: ")
        binding.rvSearchProducts.apply {
            adapter = searchAdapter
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    2, GridLayoutManager.VERTICAL,
                    false
                )
        }
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

    private fun hideCardsView() {
        Log.d(TAG, "hideCard: ")
        binding.apply {
            categoriesTv.visibility = View.GONE
            cardFurniture.visibility = View.GONE
            cardCupboard.visibility = View.GONE
            cardChair.visibility = View.GONE
            cardAccessory.visibility = View.GONE
            cardTable.visibility = View.GONE
        }
    }
}