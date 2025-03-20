package com.example.jpc_live_firebase_chat.ui.screens

import android.R.attr.label
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                content = {
                    items(chatMessages) {
                        val senderIsMe = (it.sender == currentUserId)
                        val backgroundColor = if (senderIsMe) Color.Green else Color.Blue
                        val startPadding = if (senderIsMe) 42.dp else 0.dp
                        val endPadding = if (senderIsMe) 0.dp else 42.dp

                        Card(
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 12.dp, start = startPadding, end = endPadding)
                                .background(backgroundColor),
                            content = {
                                Column(
                                    modifier = Modifier.background(backgroundColor),
                                    horizontalAlignment = if (senderIsMe) Alignment.End else Alignment.Start,
                                    content = {
                                        Text(
                                            modifier = Modifier.padding(12.dp),
                                            text = it.text,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            )
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