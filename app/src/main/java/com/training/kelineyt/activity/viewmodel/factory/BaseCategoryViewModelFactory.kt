package com.training.kelineyt.activity.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.training.kelineyt.activity.data.Category
import com.training.kelineyt.activity.viewmodel.CategoryViewModel

class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore, category) as T
    }

}