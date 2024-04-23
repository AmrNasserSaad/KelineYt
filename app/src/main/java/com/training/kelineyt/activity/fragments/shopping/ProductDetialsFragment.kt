package com.training.kelineyt.activity.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.kelineyt.R
import com.training.kelineyt.activity.adapters.ColorsAdapter
import com.training.kelineyt.activity.adapters.SizesAdapter
import com.training.kelineyt.activity.adapters.ViewPager2ImagesAdapter
import com.training.kelineyt.activity.data.CartProduct
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.util.hideBottomNavigationView
import com.training.kelineyt.activity.viewmodel.CartDetailsViewModel
import com.training.kelineyt.databinding.FragmentProductDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPager2ImagesAdapter by lazy { ViewPager2ImagesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<CartDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRV()
        setupColorsRV()
        setupViewPagerRV()

        binding.imageArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }
        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCard.setOnClickListener {
            // you can add some condition before this line to make more UX
            viewModel.addAndUpdateProductInCart(CartProduct(product,1,selectedColor,selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){

                    is Resource.Loading -> {
                        binding.buttonAddToCard.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonAddToCard.revertAnimation()
                        Toast.makeText(requireContext(), "Added to cat successfully", Toast.LENGTH_SHORT).show()
                        binding.buttonAddToCard.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is Resource.Error -> {
                        binding.buttonAddToCard.startAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }


        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = " $ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE

            if (product.sizes.isNullOrEmpty())
                tvProductSizes.visibility = View.INVISIBLE
        }

        viewPager2ImagesAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }

    }

    private fun setupSizesRV() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }
    }

    private fun setupColorsRV() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }
    }

    private fun setupViewPagerRV() {
        binding.apply {
            viewPagerProductImages.adapter = viewPager2ImagesAdapter
        }
    }
}