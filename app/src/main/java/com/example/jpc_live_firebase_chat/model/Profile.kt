package com.example.jpc_live_firebase_chat.model

import com.google.firebase.firestore.DocumentId

data class Profile(
    @DocumentId
    val profileId: String = "",
    val profileName: String = "",
    val chatColorAsHex: String = ""
)