package com.dkdevs.testing2.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dkdevs.testing2.R
import com.dkdevs.testing2.ui.uiComponents.MyAppTopBar


data class BottomNav<T : Any>(
    val name: String,
    val route: T,
    val icon: Int
)

val NavList = listOf(
    BottomNav("Dashboard", HomeNavRoutes.Dashboard, R.drawable.ic_home),
    BottomNav("Identify", HomeNavRoutes.IdentifyScreen, R.drawable.ic_camera),
    BottomNav("Wishlist", HomeNavRoutes.Saved, R.drawable.ic_bookmark),
    BottomNav("My Garden", HomeNavRoutes.MyGarden, R.drawable.ic_garden),
)

@Composable
fun MainScreen(
    redirectToDetailsScreen: (Int) -> Unit
) {

    var selectedNav by rememberSaveable {
        mutableStateOf(NavList.first().name)
    }

    val homeNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                NavList.forEach { routes ->

                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any() { it.hasRoute(route = routes.route::class) } == true,
                        onClick = {
                            selectedNav = routes.name
                            homeNavController.navigate(routes.route) {
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                                Text(text = routes.name,
                                    fontSize = 12.sp)
                        },
                        icon = {
                            Icon(painter = painterResource(id = routes.icon), contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary)
                        })
                }
            }
        }
    ) {
        HomeGraph(navHostController = homeNavController, Modifier.padding(it), redirectToDetailScreen = {
            redirectToDetailsScreen.invoke(it)
        })
    }
}


@Preview(showSystemUi = true)
@Composable
private fun prevMainScreen() {
//    MainScreen()
}