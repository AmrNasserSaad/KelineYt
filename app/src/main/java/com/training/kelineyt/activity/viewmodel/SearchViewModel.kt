package com.training.kelineyt.activity.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.training.kelineyt.activity.data.Product
import com.training.kelineyt.activity.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _searchProduct =
        MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val searchProduct = _searchProduct.asStateFlow()

    fun search(query: String) {
        Log.d(ContentValues.TAG, "search: ")
        viewModelScope.launch { _searchProduct.emit(Resource.Loading()) }

        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Products")
                .get()
                .addOnSuccessListener { result ->
                    var productsList = result.toObjects(Product::class.java)
                    productsList = productsList.filter { it.name.contains(query) }
                    viewModelScope.launch { _searchProduct.emit(Resource.Success(productsList)) }
                }.addOnFailureListener {
                    viewModelScope.launch { _searchProduct.emit(Resource.Error(it.message.toString())) }
                }
        }


    }
}
//

