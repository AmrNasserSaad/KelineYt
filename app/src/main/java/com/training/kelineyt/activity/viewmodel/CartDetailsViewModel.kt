package com.training.kelineyt.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.training.kelineyt.activity.data.CartProduct
import com.training.kelineyt.activity.firebase.FirebaseCommonUsage
import com.training.kelineyt.activity.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartDetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommonUsage: FirebaseCommonUsage

) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()


    fun addAndUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch { _addToCart.emit(Resource.Loading()) }

        firestore
            .collection("user")
            .document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id)
            .get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {
                        // Add new product

                        addNewProduct(cartProduct)
                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct) {// increase the  Quantity

                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)
                        } else {
                            // Add new product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }

            }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommonUsage.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }

        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommonUsage.increaseQuantity(documentId) { _, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }

        }
    }
}