package com.rubp.whattoeat.model

import androidx.compose.runtime.Composable

class Cell(
    val content: @Composable () -> Unit,
    val weight: Float
)