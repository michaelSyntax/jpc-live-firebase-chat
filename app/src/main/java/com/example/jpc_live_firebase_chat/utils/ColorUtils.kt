package com.example.jpc_live_firebase_chat.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt
import kotlin.random.Random
import androidx.core.graphics.toColorInt

fun randomColor(offset: Float = 0f): Color {
    return Color(
        red = Random.nextFloat() + offset,
        green = Random.nextFloat() + offset,
        blue = Random.nextFloat() + offset,
        alpha = 1f
    )
}

fun colorFromHex(hex: String): Color {
    return Color(hex.toColorInt())
}

fun Color.toHex(): String {
    val red = (red * 255).roundToInt()
    val green = (green * 255).roundToInt()
    val blue = (blue * 255).roundToInt()
    val alpha = (alpha * 255).roundToInt()
    return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
}

fun Color.complementary(): Color {
    return Color(
        red = 1.0f - red,
        green = 1.0f - green,
        blue = 1.0f - blue,
        alpha = alpha
    )
}