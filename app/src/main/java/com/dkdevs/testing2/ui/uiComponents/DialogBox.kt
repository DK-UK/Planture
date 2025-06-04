package com.dkdevs.testing2.ui.uiComponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DialogBox(
    title : String? = null,
    message : String? = null,
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
            dismissButton = {
                            TextButton(onClick = onDismiss) {
                                Text(text = "Cancel")
                            }
            },
            title = {
                if (title != null){
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            },
            text = {
                if (message != null){
                    Text(text = message , fontSize = 14.sp, fontWeight = FontWeight.Normal)
                }
            })
    }
}