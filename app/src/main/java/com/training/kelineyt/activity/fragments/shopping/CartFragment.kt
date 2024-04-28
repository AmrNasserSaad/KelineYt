package com.training.kelineyt.activity.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.training.kelineyt.R
import com.training.kelineyt.activity.adapters.CartProductAdapter
import com.training.kelineyt.activity.firebase.FirebaseCommonUsage
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.util.VerticalItemDecoration
import com.training.kelineyt.activity.viewmodel.CartViewModel
import com.training.kelineyt.databinding.FragmentCartBinding
import kotlinx.coroutines.flow.collectLatest

class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }

    /* viewmodel by [activityViewModel]
    * cuz i have toke an instance from this viewmodel before in Shopping Activity
    * لو انا جيت واخدت من ال by viewmodels هيبقي عندى اتنين اوبجيكت من نفس الفيو مودل
    * */
    private val viewModel by activityViewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRV()

        var totalPrice = 0f


        lifecycleScope.launchWhenStarted {
            viewModel.productsPrice.collectLatest { price ->
                price?.let {
                    totalPrice = it
                    binding.tvTotalPrice.text = "$ $price"
                }
            }
        }

        cartAdapter.onProductClick = {
            val bundle = Bundle().apply { putParcelable("product", it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment, bundle)
        }
        cartAdapter.onPlusClick = {
            viewModel.changeQuantity(it, FirebaseCommonUsage.QuantityChanging.INCREASE)
        }
        cartAdapter.onMinusClick = {
            viewModel.changeQuantity(it, FirebaseCommonUsage.QuantityChanging.DECREASE)
        }

        binding.imageCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }
     //send args
        binding.buttonCheckout.setOnClickListener {

            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(
                totalPrice,
                cartAdapter.differ.currentList.toTypedArray()
            )
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from Cart")
                    setMessage("Do you want to delete this item from your cart ")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()

                    }.setPositiveButton("Yes") { dialog, _ ->
                        viewModel.deleteCartProduct(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                        binding.progressbarCart.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {

                        binding.progressbarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()) {
                            showEmptyCart()
                            hideOthersViews()
                        } else {
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }

                    is Resource.Error -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun hideOthersViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyCart() {
        binding.layoutCarEmpty.visibility = View.GONE
    }

    private fun showEmptyCart() {
        binding.layoutCarEmpty.visibility = View.VISIBLE
    }

    private fun setupCartRV() {
        binding.apply {
            binding.rvCart.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = cartAdapter
                // add some space between Items
                addItemDecoration(VerticalItemDecoration())
            }
        }
    }
}