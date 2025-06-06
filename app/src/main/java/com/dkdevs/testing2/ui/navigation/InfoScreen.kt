package com.dkdevs.testing2.ui.navigation

import android.app.Activity
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.dkdevs.testing2.R
import com.dkdevs.testing2.ui.theme.Testing2Theme
import com.dkdevs.testing2.ui.utility.MyPreferences
import org.koin.androidx.compose.inject

@Preview
@Composable
private fun prevInfoScreen() {
    Testing2Theme {
        InfoScreen(){}

    }
}

@Composable
fun InfoScreen(
    redirectToMainScreen: () -> Unit
) {

    val myPref : MyPreferences by inject()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFEFF5EC), Color(0xFFD9EED8)) // Soft gradient overlay
                )
            )
            .navigationBarsPadding()

    ) {
        // Background image (illustrated)
        Image(
            painter = painterResource(id = R.drawable.info_bg), // Add this to drawable
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.4f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.height(16.dp))

                // App Logo
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo), // Add your icon
                    contentDescription = "App Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // App Name
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tagline
                Text(
                    text = stringResource(R.string.info_screen_tagline),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5C6F60),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Info Cards
                InfoItem(icon = R.drawable.ic_identify, title = stringResource(R.string.plant_identification), desc = stringResource(
                    R.string.identify_plants_using_camera_or_photos
                )
                )
                InfoItem(icon = R.drawable.ic_care, title = stringResource(R.string.care_reminders), desc = stringResource(
                    R.string.set_watering_and_care_schedules_upcoming
                )
                )
                InfoItem(icon = R.drawable.ic_book, title = stringResource(R.string.knowledge_base), desc = stringResource(
                    R.string.get_tips_on_light_soil_and_growth
                )
                )
//                InfoItem(icon = Icons.Default.Search, title = "Environment Tracker", desc = "Track humidity, temp, and light.")

//                Spacer(modifier = Modifier.height(24.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Get Started Button
                Button(
                    onClick = {
                        myPref.setInfoScreenVisitedStatus(true)
                        redirectToMainScreen.invoke()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.let_s_grow), color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer
                Text(
                    text = stringResource(R.string.info_screen_footer),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF839184)
                )
            }
        }
    }
}

@Composable
fun InfoItem(icon: Int, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4C5B4E)
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6D7F71)
            )
        }
    }
}
