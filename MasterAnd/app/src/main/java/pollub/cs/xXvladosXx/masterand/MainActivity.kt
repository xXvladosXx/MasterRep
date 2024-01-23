package pollub.cs.xXvladosXx.masterand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import pollub.cs.xXvladosXx.masterand.ui.theme.MasterAndTheme
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterAndTheme {
                navController = rememberNavController()

                SetupNavGraph(navController = navController)
            }
        }
    }
}