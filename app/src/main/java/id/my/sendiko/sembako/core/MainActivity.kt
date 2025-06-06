package id.my.sendiko.sembako.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import id.my.sendiko.sembako.core.navigation.NavGraph
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.core.preferences.dataStore
import id.my.sendiko.sembako.core.ui.theme.AppTheme
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        userPreferences = UserPreferences(this.dataStore)
        setContent {
            val dynamicTheme by userPreferences.getDynamicTheme().collectAsStateWithLifecycle(true)
            AppTheme(
                dynamicColor = dynamicTheme
            ) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController
                )
            }
        }
    }
}