package com.example.jpc_live_firebase_chat.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jpc_live_firebase_chat.ui.components.AuthTextEditField
import com.example.jpc_live_firebase_chat.ui.theme.AppTheme

@Composable
fun RegisterScreen(
    onRegisterSelection: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var valueEmail by rememberSaveable { mutableStateOf("") }
    var valuePassword by rememberSaveable { mutableStateOf("") }
    var valuePasswordConfirm by rememberSaveable { mutableStateOf("") }
    var valueUsername by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text("RegisterScreen", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))
            AuthTextEditField(
                value = valueEmail,
                icon = Icons.Default.Email,
                label = "Email",
                onValueChange = { valueEmail = it }
            )
            AuthTextEditField(
                value = valuePassword,
                icon = Icons.Default.Lock,
                label = "Password",
                onValueChange = { valuePassword = it }
            )
            AuthTextEditField(
                value = valuePasswordConfirm,
                icon = Icons.Default.Lock,
                label = "Password",
                onValueChange = { valuePasswordConfirm = it }
            )
            AuthTextEditField(
                value = valueUsername,
                icon = Icons.Default.AccountBox,
                label = "Username",
                onValueChange = { valueUsername = it }
            )
            Spacer(Modifier.height(24.dp))
            OutlinedButton(
                enabled = valuePassword == valuePasswordConfirm &&
                        valuePassword.isNotEmpty() &&
                        valueEmail.isNotEmpty() &&
                        valueUsername.isNotEmpty(),
                onClick = {
                    onRegisterSelection(valueEmail, valuePassword, valueUsername)
                },
                content = { Text("Register") }
            )
        }
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_7_PRO,
    name = "NIGHT_YES"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL,
    name = "NIGHT_NO"
)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        Surface {
            RegisterScreen(
                onRegisterSelection = { _, _, _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}