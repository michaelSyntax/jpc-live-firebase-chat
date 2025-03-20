package com.example.jpc_live_firebase_chat

import android.R
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jpc_live_firebase_chat.ui.FirebaseViewModel
import com.example.jpc_live_firebase_chat.ui.screens.ChatListScreen
import com.example.jpc_live_firebase_chat.ui.screens.LoginScreen
import com.example.jpc_live_firebase_chat.ui.screens.RegisterScreen
import kotlinx.serialization.Serializable

interface Route {
    @Serializable object LoginDestination: Route
    @Serializable object RegisterDestination: Route
    @Serializable object ChatListDestination: Route
    @Serializable data class ChatDestination(val chatId: String): Route
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatApp() {
    val navHostController = rememberNavController()
    val viewModel: FirebaseViewModel = viewModel()
    val currentUser by viewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.logout()
                        },
                        content = {
                            Icon(imageVector = Icons.Default.ExitToApp, Icons.Default.ExitToApp.name)
                        }
                    )
                }
            )
        },
        content = {
            val screenModifier = Modifier
                .padding(it)
                .fillMaxSize()
            NavHost(
                navController = navHostController,
                startDestination = Route.LoginDestination
            ) {
                composable<Route.LoginDestination> {
                    LoginScreen(
                        onLoginSelection = { email, password ->
                            viewModel.login(email, password)
                        },
                        onRegisterSelection = {
                            navHostController.navigate(Route.RegisterDestination)
                        },
                        modifier = screenModifier
                    )
                    if (currentUser != null) {
                        navHostController.navigate(Route.ChatListDestination)
                    }
                }
                composable<Route.RegisterDestination> {
                    RegisterScreen(
                        onRegisterSelection = { email, password, username ->
                            viewModel.register(email, password, username)
                        },
                        modifier = screenModifier
                    )
                    if (currentUser != null) {
                        navHostController.navigate(Route.ChatListDestination) {
                            popUpTo<Route.ChatListDestination>()
                            launchSingleTop = true
                        }
                    }
                }
                composable<Route.ChatListDestination> {
                    ChatListScreen(
                        modifier = screenModifier,

                    )
                }
                composable<Route.ChatDestination> {

                }
            }
        }
    )
}