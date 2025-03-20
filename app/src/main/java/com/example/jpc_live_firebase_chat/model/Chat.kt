package com.example.jpc_live_firebase_chat.model

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId
    val chatId: String = "",
    val messages: MutableList<Message> = mutableListOf()
)
