package com.example.jpc_live_firebase_chat.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.jpc_live_firebase_chat.model.Chat
import com.example.jpc_live_firebase_chat.model.ChatGroup
import com.example.jpc_live_firebase_chat.model.Message
import com.example.jpc_live_firebase_chat.model.Profile
import com.example.jpc_live_firebase_chat.utils.Debug
import com.example.jpc_live_firebase_chat.utils.randomColor
import com.example.jpc_live_firebase_chat.utils.toHex
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class FirebaseViewModel : ViewModel() {
    private val firebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    private var _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage = _snackBarMessage.asStateFlow()

    private var _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    private var _chatProfileList = MutableStateFlow<List<Profile>>(listOf())
    val chatProfileList = _chatProfileList.asStateFlow()

    private var _chatGroupList = MutableStateFlow<List<ChatGroup>>(listOf())
    val chatGroupList = _chatGroupList.asStateFlow()

    private var _currentChatMessages = MutableStateFlow<List<Message>>(listOf())
    val currentChatMessages = _currentChatMessages.asStateFlow()

    private var _currentChatGroupMessages = MutableStateFlow<List<Message>>(listOf())
    val currentChatGroupMessages = _currentChatGroupMessages.asStateFlow()

    private lateinit var currentProfile: Profile

    val profileCollectionReference: CollectionReference = firestore.collection("profiles")
    private lateinit var profileDocumentReference: DocumentReference
    lateinit var currentChatDocumentReference: DocumentReference

    /**
     * Groups
     */
    val chatGroupCollectionReference: CollectionReference = firestore.collection("chatGroups")
    private lateinit var chatGroupDocumentReference: DocumentReference

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
                    profileDocumentReference.set(
                        Profile(
                            profileName = profileName,
                            chatColorAsHex = randomColor().toHex()
                        )
                    )
                } else {
                    handleError(authResult.exception?.message.toString())
                }
            }
    }

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    _currentUser.value = firebaseAuth.currentUser
                    setProfileDocumentReference()
                } else {
                    handleError(authResult.exception?.message.toString())
                }
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
                currentProfile = profileList.first { it.profileId == currentUser.value!!.uid }
                profileList.removeAll { it == currentProfile }
                _chatProfileList.value = profileList
            }
        }
    }

    /** Chat */
    /**
     * FieldValue.arrayUnion: für Element zum Array hinzu.
     */
    fun sendMessage(message: String) {
        val newMessage = Message(
            text = message,
            sender = firebaseAuth.currentUser!!.uid,
            senderProfileChatColorAsHex = currentProfile.chatColorAsHex
        )
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
        currentChatDocumentReference.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val messages = value.toObject(Chat::class.java)?.messages
                if (messages != null) {
                    _currentChatMessages.value = messages
                }
            }
        }
    }

    /**
     * id1 = A
     * id2 = B
     * chatId = der Größe nach sortiert -> A-B
     */
    private fun createChatId(id1: String, id2: String): String {
        val ids = listOf(id1, id2).sorted()
        return ids.first() + ids.last()
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createGroup() {
        val uuid = Uuid.toString()
        val chatGroupId = "groupName:${firebaseAuth.currentUser!!.uid}--uuid:$uuid}"
        setChatGroupDocumentReference(chatGroupId)
    }

    fun addMemberToChatGroup(chatGroupId: String) {
        setCurrentGroupChat(chatGroupId)
        chatGroupDocumentReference.update("members", FieldValue.arrayUnion(currentProfile))
    }

    private fun setCurrentGroupChat(chatGroupId: String) {
        chatGroupDocumentReference = firestore.collection("groupChats").document(chatGroupId)
        chatGroupDocumentReference.get().addOnCompleteListener { task ->
            /**
             * Falls es noch keinen Chat mit diesem User gibt, dann erstellle einen
             * leeren Chat.
             */
            if (task.isSuccessful && task.result != null && !task.result.exists()) {
                chatGroupDocumentReference.set(Chat())
            }
        }
        chatGroupDocumentReference.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val messages = value.toObject(Chat::class.java)?.messages
                if (messages != null) {
                    _currentChatGroupMessages.value = messages
                }
            }
        }
    }

    private fun setChatGroupDocumentReference(chatGroupId: String) {
        chatGroupDocumentReference =
            chatGroupCollectionReference.document(chatGroupId)

        chatGroupDocumentReference.get().addOnCompleteListener { task ->
            /**
             * Falls es diese ChatGroup noch nicht gibt, dann erstellle eine.
             */
            if (task.isSuccessful && task.result != null && !task.result.exists()) {
                val members = mutableListOf(currentProfile)
                chatGroupDocumentReference.set(ChatGroup(chatGroupId = chatGroupId, members = members))
            }
        }

        chatGroupCollectionReference.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                val chatGroupList = value.map { it.toObject(ChatGroup::class.java) }.toMutableList()
                _chatGroupList.value = chatGroupList
            }
        }
    }

    fun sendGroupChatMessage(message: String) {
        val newMessage = Message(
            text = message,
            sender = firebaseAuth.currentUser!!.uid,
            senderProfileChatColorAsHex = currentProfile.chatColorAsHex
        )
        chatGroupDocumentReference.update("messages", FieldValue.arrayUnion(newMessage))
    }
}