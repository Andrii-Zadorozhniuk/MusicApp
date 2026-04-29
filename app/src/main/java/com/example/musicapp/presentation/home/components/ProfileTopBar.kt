package com.example.musicapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.musicapp.presentation.common.CategoryButton
import com.example.musicapp.ui.theme.Background
import com.example.musicapp.ui.theme.CategoryButton
import com.example.musicapp.ui.theme.Green
import com.example.musicapp.ui.theme.TextColor
import com.example.musicapp.ui.theme.UnselectedColor

@Composable
fun ProfileTopBar(selectedCategory: MutableState<String> = mutableStateOf("All")) {

    val categories = listOf("All", "Trending", "New Releases")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null, tint = TextColor,
            modifier = Modifier.size(34.dp)
                .clip(CircleShape)
                .background(color = Color.Gray.copy(alpha = 0.1f))
                .padding(4.dp)
        )
        categories.forEach { category ->
            CategoryButton(
                text = category,
                isSelected = selectedCategory.value == category,
                onClick = { selectedCategory.value = category }
            )
        }
    }
}


