package com.example.jpc_live_firebase_chat.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jpc_live_firebase_chat.ui.theme.AppTheme

@Composable
fun AuthTextEditField(
    value: String,
    icon: ImageVector,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.padding(horizontal = 24.dp),
        value = value,
        onValueChange = {onValueChange(it) },
        singleLine = true,
        leadingIcon = {
            Icon(icon, icon.name)
        },
        trailingIcon = {
            IconButton(
                onClick = { onValueChange("") },
                content = {
                    if (value.isNotEmpty()) {
                        Icon(Icons.Default.Close, Icons.Default.Close.name)
                    }
                }
            )
        },
        label = { Text(label) }
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
    var value by remember { mutableStateOf("") }
    AppTheme {
        Surface {
            AuthTextEditField(
                value = value,
                icon = Icons.Default.Email,
                onValueChange = { value = it },
                label = "Email"
            )
        }
    }
}