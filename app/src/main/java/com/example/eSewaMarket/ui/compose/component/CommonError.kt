package com.example.eSewaMarket.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.eSewaMarket.R

@Composable
fun CommonError(
    message: String,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.run {
            fillMaxSize()
                .padding(24.dp)
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = colorResource(R.color.text_dark_400)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Something went wrong",
            color = colorResource(R.color.text_dark_400)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            color = colorResource(R.color.text_dark_200)
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRetry
            ) {
                Text("Retry")
            }
        }
    }
}