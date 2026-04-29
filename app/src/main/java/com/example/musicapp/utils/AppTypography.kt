package com.example.musicapp.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.musicapp.ui.theme.TextColor
import com.example.musicapp.ui.theme.Typography

val appTypography = androidx.compose.material3.Typography(
    displayLarge = Typography.displayLarge.copy(fontFamily = appFontFamily, color = TextColor, fontSize = 100.sp),
    displayMedium = Typography.displayMedium.copy(fontFamily = appFontFamily, color = TextColor),
    displaySmall = Typography.displaySmall.copy(fontFamily = appFontFamily, color = TextColor),
    titleLarge = Typography.titleLarge.copy(fontFamily = appFontFamily, color = TextColor, fontSize = 26.sp),
    titleMedium = Typography.titleMedium.copy(fontFamily = appFontFamily, color = TextColor, fontSize = 19.sp),
    titleSmall = Typography.titleSmall.copy(fontFamily = appFontFamily, color = TextColor),
    bodyLarge = Typography.bodyLarge.copy(fontFamily = appFontFamily, color = TextColor),
    bodyMedium = Typography.bodyMedium.copy(fontFamily = appFontFamily, color = TextColor),
    bodySmall = Typography.bodySmall.copy(fontFamily = appFontFamily, color = TextColor),
)