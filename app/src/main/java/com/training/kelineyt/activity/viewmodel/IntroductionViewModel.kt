package com.training.kelineyt.activity.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.training.kelineyt.R
import com.training.kelineyt.activity.util.Constants.INTRODUTION_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(

    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth

) : ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object {
        const val SHOPPING_ACTIVITY = 23
        val ACCOUNT_OPTIONS_FRAGMENT = R.id.action_introductionFragment2_to_accountOptionsFragment
    }

    init {

        val isButtonClicked = sharedPreferences.getBoolean(INTRODUTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }

        } else if (isButtonClicked) {
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
            }
        } else Unit

    }

    fun startBtnClick() {
        sharedPreferences.edit().putBoolean(INTRODUTION_KEY, true).apply()

    }
}