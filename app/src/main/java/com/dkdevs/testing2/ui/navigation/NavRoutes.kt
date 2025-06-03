package com.dkdevs.testing2.ui.navigation

import kotlinx.serialization.Serializable


sealed class MainNavRoutes {

    @Serializable
    data object SplashScreen : MainNavRoutes()

    @Serializable
    data object InfoScreen : MainNavRoutes()

    @Serializable
    data object MainScreen : MainNavRoutes()

    @Serializable
    data object Settings : MainNavRoutes()

    @Serializable
    data class PlantDetailsScreen(var id : Int) : MainNavRoutes()
}


sealed class HomeNavRoutes() {
    @Serializable
    data object Dashboard : HomeNavRoutes()

    @Serializable
    data object IdentifyScreen : HomeNavRoutes()

    @Serializable
    data object Saved : HomeNavRoutes()

    @Serializable
    data object MyGarden : HomeNavRoutes()

}