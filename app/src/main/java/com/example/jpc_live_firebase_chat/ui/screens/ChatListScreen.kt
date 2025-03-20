package com.example.jpc_live_firebase_chat.ui.screens

import android.R.attr.text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jpc_live_firebase_chat.model.Profile

@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    chatProfileList: List<Profile>,
    onProfileSelection: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        content = {
            items(chatProfileList) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable(onClick = { onProfileSelection(it.profileId) }),
                    content = {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = it.profileName,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }
    )
}