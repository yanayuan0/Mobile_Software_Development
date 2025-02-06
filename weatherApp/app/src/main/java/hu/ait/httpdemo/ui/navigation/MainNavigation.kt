package hu.ait.httpdemo.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object MoneyRatesAPI : Screen("moneyrates")
    object NasaMarsAPI : Screen("nasamars")
    object WeatherAPI : Screen("weather")
}