package com.example.jpc_live_firebase_chat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jpc_live_firebase_chat.model.Message
import com.example.jpc_live_firebase_chat.ui.components.AuthTextEditField

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    currentUserId: String,
    chatMessages: List<Message>,
    sendMessage: (String) -> Unit
) {
    var valueMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(5f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chatMessages) {
                    val senderIsMe = (it.sender == currentUserId)
                    val backgroundColor = if (senderIsMe) Color.Blue else Color.Red
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = if (senderIsMe) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Text(
                            text = it.text,
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = backgroundColor,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp) // Innenabstand für bessere Optik
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp).weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    AuthTextEditField(
                        value = valueMessage,
                        icon = Icons.Default.MailOutline,
                        label = "message",
                        onValueChange = { valueMessage = it }
                    )
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = {
                            sendMessage(valueMessage)
                            valueMessage = ""
                        },
                        content = { Text("Send") }
                    )
                }
            )
        }
    )
}
