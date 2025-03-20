package com.example.jpc_live_firebase_chat.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.jpc_live_firebase_chat.model.Chat
import com.example.jpc_live_firebase_chat.model.Message
import com.example.jpc_live_firebase_chat.model.Profile
import com.example.jpc_live_firebase_chat.utils.Debug
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirebaseViewModel : ViewModel() {
    private val firebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    private var _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage = _snackBarMessage.asStateFlow()

    private var _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    private var _chatProfileList = MutableStateFlow<List<Profile>>(listOf())
    val chatProfileList = _chatProfileList.asStateFlow()

    val profileCollectionReference: CollectionReference = firestore.collection("profiles")
    private lateinit var profileDocumentReference: DocumentReference
    lateinit var currentChatDocumentReference: DocumentReference

    init {
        if (firebaseAuth.currentUser != null) {
            setProfileDocumentReference()
        }
    }

    /** Auth */
    fun register(email: String, password: String, profileName: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _currentUser.value = firebaseAuth.currentUser
                    setProfileDocumentReference()
                    profileDocumentReference.set(Profile(profileName = profileName))
                } else {
                    handleError(authResult.exception?.message.toString())
                }
            }
    }

    fun login(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        _currentUser.value = firebaseAuth.currentUser
                        setProfileDocumentReference()
                    } else {
                        handleError(authResult.exception?.message.toString())
                    }
                }
        } else {
            handleError(Debug.AUTH_ERROR_MESSAGE.value)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = firebaseAuth.currentUser
    }

    fun resetSnackbarMessage() {
        _snackBarMessage.value = null
    }

    private fun handleError(message: String) {
        showSnackBarMessage(message)
        logError(message)
    }

    private fun showSnackBarMessage(message: String) {
        _snackBarMessage.value = message
    }

    private fun logError(message: String) {
        Log.e(Debug.AUTH_TAG.value, message)
    }

    private fun setProfileDocumentReference() {
        profileDocumentReference =
            profileCollectionReference.document(firebaseAuth.currentUser!!.uid)
        profileCollectionReference.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val profileList = value.map { it.toObject(Profile::class.java) }.toMutableList()
                profileList.removeAll { it.profileId == currentUser.value!!.uid }
            }
        }
    }

    /** Chat */
    /**
     * FieldValue.arrayUnion: für Element zum Array hinzu.
     */
    fun sendMessage(message: String) {
        val newMessage = Message(message, firebaseAuth.currentUser!!.uid)
        currentChatDocumentReference.update("messages", FieldValue.arrayUnion(newMessage))
    }

    fun setCurrentChat(chatPartnerId: String) {
        val chatId = createChatId(chatPartnerId, currentUser.value!!.uid)
        currentChatDocumentReference = firestore.collection("chats").document(chatId)
        currentChatDocumentReference.get().addOnCompleteListener { task ->
            /**
             * Falls es noch keinen Chat mit diesem User gibt, dann erstellle einen
             * leeren Chat.
             */
            if (task.isSuccessful && task.result != null && !task.result.exists()) {
                currentChatDocumentReference.set(Chat())
            }
        }
    }

    /**
     * id1 = Sender
     * id2 = Receiver
     * chatId = BA wenn wir sender sind.
     * und wenn ChartParnter Sender ist: chatID AB
     */
    private fun createChatId(id1: String, id2: String): String {
        val ids = listOf(id1, id2).sorted()
        return ids.first() + ids.last()
    }
}