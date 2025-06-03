package com.dkdevs.testing2.ui.uiComponents

import android.app.UiModeManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dkdevs.testing2.R
import com.dkdevs.testing2.ui.theme.Testing2Theme
import com.dkdevs.testing2.ui.theme.plantColors

@Composable
fun MyAppTopBar(
    modifier: Modifier = Modifier,
    isBackAvailable: Boolean = false,
    backIcon: ImageVector = Icons.Default.ArrowBack,
    title: String?,
    onBackPressed: () -> Unit = {},
    postIcons: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        ) {

            Text(
                text = title ?: stringResource(id = R.string.app_name),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            Row {
                postIcons()
            }
        }

        if (isBackAvailable) {

            Icon(
                imageVector = backIcon, contentDescription = "back button",
                tint = Color.White,
                modifier = Modifier
                    .clickable {
                        onBackPressed.invoke()
                    }
                    .padding(10.dp)
            )
        }
        else {
            Image(painter = painterResource(id = R.drawable.ic_logo), contentDescription = "app logo",
                modifier = Modifier.padding(10.dp))
        }
    }

}

@Preview(uiMode = UiModeManager.MODE_NIGHT_NO, showSystemUi = true)
@Preview(uiMode = UiModeManager.MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun prevTopbar() {
    Testing2Theme {
        MyAppTopBar(title = "Planture")
    }
}