package com.training.kelineyt.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.training.kelineyt.activity.data.CartProduct
import com.training.kelineyt.activity.firebase.FirebaseCommonUsage
import com.training.kelineyt.activity.helper.getProductPrice
import com.training.kelineyt.activity.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommonUsage: FirebaseCommonUsage
) : ViewModel() {

    private val _cartProducts =
        MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()


    val productsPrice = cartProducts.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }

            else -> null
        }
    }
    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()
    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id
            firestore.collection("user").document(auth.uid!!)
                .collection("cart").document(documentId).delete()
        }
    }

    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage
                .getProductPrice(cartProduct.product.price) * cartProduct.quantity)
                .toDouble()

        }.toFloat()
    }


    init {
        getCartProducts()
    }

    // fetch and retrieve the cart product from cart collection
    private fun getCartProducts() {
        viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            //to notify the cart collection (like callback)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch { _cartProducts.emit(Resource.Error(error?.message.toString())) }
                } else {
                    cartProductDocuments = value.documents
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch { _cartProducts.emit(Resource.Success(cartProducts)) }
                }

            }
    }


    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommonUsage.QuantityChanging
    ) {

        val index = cartProducts.value.data?.indexOf(cartProduct)
        /**
         * index could be equal to -1 if the function [getCartProducts] delays
         * which will also delay the result  we expect to be inside
         * the [_cartProducts] and to prevent the app from crashing we make a check
         */
        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id

            when (quantityChanging) {
                FirebaseCommonUsage.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit((Resource.Loading())) }
                    increaseQuantity(documentId)
                }

                FirebaseCommonUsage.QuantityChanging.DECREASE -> {
                    if (cartProduct.quantity == 1) {
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit((Resource.Loading())) }
                    decreaseQuantity(documentId)
                }
            }

        }

    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommonUsage.decreaseQuantity(documentId) { result, exception ->
            // we implement just the error case cuz the success case is already implement in fun [getCartProducts]
            if (exception != null)
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommonUsage.increaseQuantity(documentId) { result, exception ->
            // we implement just the error case cuz the success case is already implement in fun [getCartProducts]
            if (exception != null)
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
        }
    }

}