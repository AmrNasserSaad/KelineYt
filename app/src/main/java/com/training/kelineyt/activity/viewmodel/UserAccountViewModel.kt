package com.training.kelineyt.activity.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.training.kelineyt.activity.KelineApplication
import com.training.kelineyt.activity.data.User
import com.training.kelineyt.activity.util.RegisterValidation
import com.training.kelineyt.activity.util.Resource
import com.training.kelineyt.activity.util.validateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(

    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    application: Application

) : AndroidViewModel(application) {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    private val _updateInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateInfo = _updateInfo.asStateFlow()

    init {
        getUserInfoFromFBS()
    }

    fun getUserInfoFromFBS() {

        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Resource.Success(it))
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }

    }


    fun updateUserInfo(user: User, imageUri: Uri?) {

        viewModelScope.launch {
            _updateInfo.emit(Resource.Loading())
        }


        val areInputsValid = validateEmail(user.email) is RegisterValidation.Success
                && user.fristName.trim().isNotEmpty()
                && user.lastName.trim().isNotEmpty()

        if (!areInputsValid) {
            viewModelScope.launch {
                _user.emit(Resource.Error("Check Your Inputs"))
            }
            return
        }


        if (imageUri == null) {
            // user not upload a new image
            saveUserInformation(user, true)
        } else {
                // user upload a new image
            saveUserInformationWithNewImage(user, imageUri)
        }


    }

    // if user upload a new image that send the new image with user infos
    private fun saveUserInformationWithNewImage(user: User, imageUri: Uri) {
        viewModelScope.launch {
            try {
                    // Select image from gallery fun
                val imageBitmap = MediaStore.Images
                    .Media.getBitmap(
                        getApplication<KelineApplication>().contentResolver,
                        imageUri
                    )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory =
                    storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imageUrl), false)

            } catch (e: Exception) {
                viewModelScope.launch {
                    _updateInfo.emit(Resource.Error(e.message.toString()))
                }

            }
        }
    }

    // if user not upload a new image that keep the old image with user infos
    private fun saveUserInformation(user: User, shouldRetrieveOldImage: Boolean) {

        firestore.runTransaction { transition ->
            val documentRef = firestore.collection("user").document(auth.uid!!)
            if (shouldRetrieveOldImage) {
                val currentUser = transition.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                transition.set(documentRef, newUser)
            } else {
                transition.set(documentRef, user)

            }

        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateInfo.emit(Resource.Error(it.message.toString()))
            }
        }


    }

}