package com.example.jpc_live_firebase_chat.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.jvm.java

class FirebaseViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val firebaseDB = Firebase.firestore

    private lateinit var currentUserId: String

    init {
        if (auth.currentUser != null) {
            currentUserId = auth.currentUser!!.uid
        }
    }

    // Auth
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUser.value = auth.currentUser
                currentUserId = auth.currentUser!!.uid
            } else {
                Log.e("AUTH", it.exception?.localizedMessage.toString())
            }
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUser.value = auth.currentUser
                currentUserId = auth.currentUser!!.uid
            } else {
                Log.e("AUTH", it.exception?.localizedMessage.toString())
            }
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }

    // Firebase Firestore
}