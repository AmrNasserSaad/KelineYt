package com.training.kelineyt.activity.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.training.kelineyt.activity.firebase.FirebaseCommonUsage
import com.training.kelineyt.activity.util.Constants.INTRODUTION_SP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun ProvideFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun ProvideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    fun ProvideIntroductionSP(application: Application) =
        application.getSharedPreferences(INTRODUTION_SP, MODE_PRIVATE)

    @Provides
    @Singleton
    fun ProvideFirebaseCommonUsage(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = FirebaseCommonUsage(firestore,firebaseAuth)


    @Provides
    @Singleton
    fun ProvideStorage() = FirebaseStorage.getInstance().reference

}