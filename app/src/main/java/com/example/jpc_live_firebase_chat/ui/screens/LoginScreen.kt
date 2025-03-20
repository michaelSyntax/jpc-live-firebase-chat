package com.example.jpc_live_firebase_chat.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.jpc_live_firebase_chat.ui.components.AuthTextEditField
import com.example.jpc_live_firebase_chat.ui.theme.AppTheme

@Composable
fun LoginScreen(
    onLoginSelection: (String, String) -> Unit,
    onRegisterSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    var queryEmail by rememberSaveable { mutableStateOf("") }
    var queryPassword by rememberSaveable { mutableStateOf("") }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text("LoginScreen", style = MaterialTheme.typography.headlineSmall)
            AuthTextEditField(
                value = queryEmail,
                icon = Icons.Default.Email,
                label = "Email",
                onValueChange = { queryEmail = it }
            )
            AuthTextEditField(
                value = queryPassword,
                icon = Icons.Default.Lock,
                label = "Password",
                onValueChange = { queryPassword = it }
            )
            TextButton(
                onClick = { onRegisterSelection() },
                content = {
                    Text("Noch kein Account? Register hier ->")
                }
            )
            OutlinedButton(
                onClick = {
                    onLoginSelection(queryEmail, queryPassword)
                },
                content = {Text("Login")}
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
            LoginScreen(
                onLoginSelection = {_, _ ->},
                onRegisterSelection = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}