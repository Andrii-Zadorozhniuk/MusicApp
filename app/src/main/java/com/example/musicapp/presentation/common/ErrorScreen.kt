package com.example.musicapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.theme.Green
import com.example.musicapp.ui.theme.UnselectedColor

@Composable
fun ErrorScreen(
    text: String = "Check your internet connection.",
    onRetry: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = text)
        Button(onClick = {onRetry()}, colors = ButtonDefaults.buttonColors(containerColor = UnselectedColor)) {
            Text("Retry")
        }
    }
}