package com.plcoding.nativeiosincompose

import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.material.MaterialTheme

@Composable
actual fun NativeButton(onClick: () -> Unit, isLoading: Boolean, modifier: Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colors.onPrimary
            )
        } else {
            Text("Click")
        }

    }
}