package com.example.jpc_live_firebase_chat.model

import com.google.firebase.firestore.DocumentId

data class ChatGroup(
    @DocumentId
    val chatGroupId: String = "",
    val members: MutableList<Profile> = mutableListOf()
)