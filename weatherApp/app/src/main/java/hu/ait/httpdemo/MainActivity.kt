package hu.ait.httpdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.httpdemo.ui.navigation.Screen
import hu.ait.httpdemo.ui.screen.MainScreen
import hu.ait.httpdemo.ui.screen.city.CityViewModel
import hu.ait.httpdemo.ui.screen.money.MoneyApiScreen
import hu.ait.httpdemo.ui.screen.weather.WeatherApiScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val cityViewModel: CityViewModel = hiltViewModel()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                NavGraph(
                    modifier = Modifier.padding(innerPadding),
                    cityViewModel = cityViewModel
                )
            }
        }
    }
}


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    cityViewModel: CityViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onWeatherAPISelected = { cityName ->
                    navController.navigate("${Screen.WeatherAPI.route}/$cityName")
                },
                cityViewModel = cityViewModel
            )
        }
        // Other routes
        composable(
            route = "${Screen.WeatherAPI.route}/{cityName}",
            arguments = listOf(navArgument("cityName") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            WeatherApiScreen(
                navController = navController,
                cityName = cityName,
                cityViewModel = cityViewModel
            )
        }
    }
}
