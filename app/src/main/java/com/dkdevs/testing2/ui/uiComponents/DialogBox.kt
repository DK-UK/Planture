package com.dkdevs.testing2.ui.uiComponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DialogBox(
    title : String = "",
    message : String = "",
    show : Boolean,
    onDismiss : () -> Unit,
    onConfirm : () -> Unit
) {
    if (show){
        AlertDialog(onDismissRequest = { 
                                       onDismiss.invoke()
                                       }, confirmButton = { 
                                           Button(onClick = onConfirm) {
                                               Text(text = "Confirm")
                                           }
                                       },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            })
    }
}