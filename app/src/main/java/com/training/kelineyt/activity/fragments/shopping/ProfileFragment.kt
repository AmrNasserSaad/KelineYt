package com.training.kelineyt.activity.fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.training.kelineyt.R
import com.training.kelineyt.activity.activities.LoginRegisterActivity
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.util.showBottomNavigationView
import com.training.kelineyt.activity.viewmodel.ProfileViewModel
import com.training.kelineyt.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigation()

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath)
                            .error(ColorDrawable(Color.BLACK)).into(binding.imageUser)

                        binding.tvUserName.text = "${it.data.fristName} ${it.data.lastName} "
                    }

                    is Resource.Error -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()

        showBottomNavigationView()
    }

    private fun navigation() {
        binding.constraintProfile.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }
        binding.linearAllOrders.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
        }
        binding.linearBilling.setOnClickListener {

            val action =
                ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                    0f,
                    emptyArray(),
                    false
                )
            findNavController().navigate(action)
        }
        binding.linearLogOut.setOnClickListener {

            showAlertDialog()


        }

        binding.linearLanguage.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_languageFragment)
        }


    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setMessage("Do you want to Sign Out ")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            // Code to be executed when the positive button is clicked
            viewModel.logout()
            val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Code to be executed when the negative button is clicked
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


}