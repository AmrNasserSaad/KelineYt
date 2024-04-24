package com.training.kelineyt.activity.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.training.kelineyt.R
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.viewmodel.CartViewModel
import com.training.kelineyt.databinding.ActivityShoppingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy { ActivityShoppingBinding.inflate(layoutInflater) }

    val viewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.soppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        countNumberOfTheBadge()


    }

    private fun countNumberOfTheBadge() {
        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        val count = it.data?.size ?: 0
                        binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment)
                            .apply {
                                number = count
                                backgroundColor = resources.getColor(R.color.g_blue)
                            }
                    }

                    else -> Unit
                }
            }
        }
    }
}