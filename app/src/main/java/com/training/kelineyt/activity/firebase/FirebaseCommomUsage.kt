package com.training.kelineyt.activity.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.training.kelineyt.activity.data.CartProduct

class FirebaseCommonUsage(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

) {

    private val cartCollection = firestore
        .collection("user")
        .document(auth.uid!!)
        .collection("cart")

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {

        cartCollection
            .document()
            .set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }
            .addOnFailureListener {
                onResult(null, it)

            }

    }

    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {

        firestore.runTransaction { transition ->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                // copy() is a extension fun that copy your object and change one attribute (as u wish)
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }


    }
}