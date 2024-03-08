package com.training.kelineyt.activity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.training.kelineyt.R
import com.training.kelineyt.activity.activities.ShoppingActivity
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.viewmodel.LoginViewModel
import com.training.kelineyt.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login){

    private lateinit var  binding : FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel >()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAccountRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment)
        }

        binding.apply {
            buttonLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPassword.text.toString()
                viewModel.login(email,password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it)
                {
                    is Resource.Loading -> {

                        binding.buttonLogin.startAnimation()

                    }
                    is Resource.Success -> {
                        binding.buttonLogin.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.buttonLogin.revertAnimation()

                    }
                    else -> Unit
                }
            }
        }
    }

}