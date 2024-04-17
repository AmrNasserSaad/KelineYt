package com.training.kelineyt.activity.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.training.kelineyt.R
import com.training.kelineyt.activity.activities.ShoppingActivity


fun Fragment.hideBottomNavigationView(){
    val bottomNavigationBarView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigationBarView.visibility = View.GONE
}
fun Fragment.showBottomNavigationView(){
    val bottomNavigationBarView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavigationBarView.visibility = View.VISIBLE
}
