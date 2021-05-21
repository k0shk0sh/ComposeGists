package com.fastaccess.gists.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextView(
    text: String,
) = Text(
    text = text,
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
)

@Composable
fun CenteredBox(content: @Composable() () -> Unit) =
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        content.invoke()
    }