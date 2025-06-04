package com.dkdevs.testing2.ui.navigation

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dkdevs.testing2.R
import com.dkdevs.testing2.ui.theme.Testing2Theme
import com.dkdevs.testing2.ui.theme.plantColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier : Modifier = Modifier,
    redirectToMainScreen : () -> Unit
) {

    produceState(initialValue = false) {
        var timer = 0

        while (true){
            if (timer > 2000){
                value = true
                redirectToMainScreen.invoke()
            }
            timer += 1000
            delay(1000)
        }
    }

//    val view = LocalView.current
//    val window = (view.context as Activity).window
//
//    WindowCompat.setDecorFitsSystemWindows(window, false)
//    window.statusBarColor = Color.Transparent.toArgb()
//    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

    val owner = LocalLifecycleOwner.current

  /*  DisposableEffect(owner) {
        val observer = LifecycleEventObserver {owner, event->
            when(event) {
                Lifecycle.Event.ON_CREATE -> TODO()
                Lifecycle.Event.ON_START -> {

                }
                Lifecycle.Event.ON_RESUME -> TODO()
                Lifecycle.Event.ON_PAUSE -> TODO()
                Lifecycle.Event.ON_STOP -> TODO()
                Lifecycle.Event.ON_DESTROY -> TODO()
                Lifecycle.Event.ON_ANY -> TODO()
                else -> {}
            }
        }

        owner.lifecycle.addObserver(observer)

        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
*/

    Box(modifier = modifier
        .fillMaxSize()
        .background(color = MaterialTheme.plantColors.cardBackground),
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logo), // Add your icon
                contentDescription = "App Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // App Name
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun prevSplashScreen() {
    Testing2Theme {
        SplashScreen {  }
    }
}