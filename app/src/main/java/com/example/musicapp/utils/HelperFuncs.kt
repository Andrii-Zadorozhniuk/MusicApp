package com.example.musicapp.utils

import androidx.compose.ui.unit.Dp

class HelperFuncs {
    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    fun lerp(start: Dp, stop: Dp, fraction: Float): Dp {
        return start + (stop - start) * fraction
    }

    fun lerp(start: Float, stop: Float, fraction: Float): Float {
        return start + (stop - start) * fraction
    }


}