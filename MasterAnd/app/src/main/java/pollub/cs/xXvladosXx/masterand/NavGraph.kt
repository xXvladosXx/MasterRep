package pollub.cs.xXvladosXx.masterand

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "login_screen" ){

        composable(route = Screen.Login.route){
            LoginScreenInitial(navController = navController)
        }

        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("colors"){
                    type = NavType.IntType
                    defaultValue = 6
                }
            )
        ){ entry ->
            GameScreenInitial(navController = navController, colors = entry.arguments?.getInt("colors") ?: 6)
        }

        composable(route = Screen.Profile.route){
            ProfileScreenInitial(navController = navController)
        }
    }
}
