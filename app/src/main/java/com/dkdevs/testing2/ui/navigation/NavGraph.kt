package com.dkdevs.testing2.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dkdevs.testing2.data.repo.PlantDestinationType


@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier : Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController, startDestination = MainNavRoutes.SplashScreen) {

        composable<MainNavRoutes.SplashScreen> {
            SplashScreen {isVisited->
                // redirect to info if user visits first time
                if (!isVisited){
                    navHostController.navigateToInfoScreen(true)
                }
                else {
                    navHostController.navigateToMainScreen(true)
                }
                // redirect to main home screen
            }
        }

        composable<MainNavRoutes.InfoScreen> {
            InfoScreen {
                navHostController.navigateToMainScreen(true)
            }
        }

       composable<MainNavRoutes.MainScreen> {
            MainScreen {
                navHostController.navigateToDetailScreen(it)
            }
       }

        composable<MainNavRoutes.PlantDetailsScreen> {
            val plantId = it.toRoute<MainNavRoutes.PlantDetailsScreen>().id ?: 0
            PlantDetailScreen(plantId = plantId) {
                navHostController.popBackStack()
            }

        }

    }
}

@Composable
fun HomeGraph(
    bottomPadding : PaddingValues,
    navHostController: NavHostController,
    redirectToDetailScreen : (Int) -> Unit
) {
    NavHost(
        navController = navHostController, startDestination = HomeNavRoutes.Dashboard) {
        composable<HomeNavRoutes.Dashboard> {
            DashboardScreen(Modifier.padding(PaddingValues(bottom = bottomPadding.calculateBottomPadding()))){
                redirectToDetailScreen.invoke(it)
            }
        }

        composable<HomeNavRoutes.IdentifyScreen> {
            IdentifyScreen() {
                redirectToDetailScreen.invoke(it)
            }
        }

        composable<HomeNavRoutes.Saved> {
            PlantListScreen(getPlantsFrom = PlantDestinationType.FromWishlist) {
                redirectToDetailScreen.invoke(it)
            }
        }

        composable<HomeNavRoutes.MyGarden> {
            PlantListScreen(getPlantsFrom = PlantDestinationType.FromMyGarden) {
                redirectToDetailScreen.invoke(it)
            }
        }
    }
}

fun NavHostController.navigateToInfoScreen(popBack : Boolean = false) {
    this.navigate(MainNavRoutes.InfoScreen){
        if (popBack) {
            popBackStack()
        }
    }
}

fun NavHostController.navigateToMainScreen(popBack : Boolean = false) {
    this.navigate(MainNavRoutes.MainScreen){
        if (popBack) {
            popBackStack()
        }
    }
}

fun NavHostController.navigateToDetailScreen(id : Int) {
    this.navigate(MainNavRoutes.PlantDetailsScreen(id)){

        popUpTo(graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true

    }
}

