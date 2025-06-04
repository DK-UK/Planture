package com.dkdevs.testing2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dkdevs.testing2.ui.navigation.NavGraph
import com.dkdevs.testing2.ui.theme.Testing2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            Testing2Theme {
                NavGraph(navHostController = navController, modifier = Modifier)
            }
        }
    }
}

