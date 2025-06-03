package com.dkdevs.testing2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FontDemo() {
    Scaffold {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            Column {
                Text(text = "This is a first sentence with fontScale : ${LocalDensity.current.fontScale}",
                    fontSize = 18.sp * LocalDensity.current.fontScale,
                    style = TextStyle(
                    )
                )
                Text(text = "This is a second sentence with fontScale : ${LocalDensity.current.fontScale}",
                    fontSize = 16.sp * LocalDensity.current.fontScale)

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = "This is a first sentence without fontScale : ${LocalDensity.current.fontScale}",
                    fontSize = 18.sp,
                    style = TextStyle(
                    )
                )
                Text(text = "This is a second sentence without fontScale : ${LocalDensity.current.fontScale}",
                    fontSize = 16.sp)
            }
        }
    }
}

@Preview(fontScale = 0.85f, heightDp = 100)
@Preview(fontScale = 0.90f, heightDp = 100)
@Preview(fontScale = 1f, heightDp = 100)
@Composable
private fun prevFontDemo() {
    FontDemo()
}